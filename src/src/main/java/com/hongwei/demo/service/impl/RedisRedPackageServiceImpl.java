package com.hongwei.demo.service.impl;

import com.hongwei.demo.dao.IRedPackageDao;
import com.hongwei.demo.dao.IRedisDao;
import com.hongwei.demo.dao.IUserRedPackageDao;
import com.hongwei.demo.entity.UserRedPackageEntity;
import com.hongwei.demo.service.IRedisRedPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/***
 * 基于redis缓存服务的红包业务接口
 * 流程：
 *      1、判断是否存在可抢的库存，如果已经没有可争抢的红包，返回0
 *      2、有可争夺的红包，对于红包库存递减，重新设置该红包的总库存
 *      3、将抢到的红包信息存储到链表中，链表的key为red_packets_{id}
 *      4、如果当前库存为0则返回2，表示已全部抢完，此时可以触发数据库对于redis链表数据的持久化操作
 *          链表的键名为red_packets_{id}，它保存了参与抢红包的用户名与抢的时间
 *      5、如果库存不为0，说明还有红包，返回1表示用户此次参与抢红包操作成功
 *  触发条件已经明确，实际触发操作要考虑现实业务场景，链表的数据量是非常大的，一次性持久化会影响用户体验，建议使用异步消息队列让其他工作机完成
 *  如果红包在规定发放时间没有被均摊完，例如发放2万份，预计10号结束活动，到了10号红包只发放出去1.5万份，此时需要设计一个定时任务来定时结束活动，按最后实际发放数量为准
 *  以上流程使用LUA脚本来执行，LUA脚本具备天生的原子性操作，非常适合用在类似本业务场景的高并发模块中，配合redis缓存服务器实现支撑高并发业务的基础架构
 *  此种做法具备一定风险，因redis持久化时间间隔问题，在此期间一旦流量大于预估容量非常容易导致雪崩，应充分调查流量预期峰值，做好缓存架构
 */
@Service
public class RedisRedPackageServiceImpl implements IRedisRedPackageService {
    /**
     * 存储在缓存服务器中的数据表键名
     */
    private static final String LIST_KEY_PREFIX = "red_packets_";
    /**
     * 每次最多拉取多少条数据
     */
    private static final Integer FETCH_MAX_SIZE = 1000;

    private final IRedisDao redisDao;
    private final IRedPackageDao redPackageDao;
    private final IUserRedPackageDao userRedPackageDao;

    @Autowired
    public RedisRedPackageServiceImpl(IRedisDao redisDao, IRedPackageDao redPackageDao, IUserRedPackageDao userRedPackageDao) {
        this.redisDao = redisDao;
        this.redPackageDao = redPackageDao;
        this.userRedPackageDao = userRedPackageDao;
    }

    /**
     * 保存红包信息到redis服务
     *
     * @param pid
     * @param unitAmount
     */
    @Override
    @Async
    public int add(Integer pid, Double unitAmount) {
        Long begin = System.currentTimeMillis();
        int count = 0;
        // 获取红包链表
        BoundListOperations ops = redisDao.boundListOps(LIST_KEY_PREFIX + pid);
        // 计算数据表大小
        Long size = ops.size();
        Long fetchSize = computeSize(size);
        List<UserRedPackageEntity> userRedPackages = new ArrayList<>(FETCH_MAX_SIZE);
        for (int i = 0; i < fetchSize; i++) {
            // 获取用户id集合
            List userIdList = getUserIdList(ops, i);
            // 生成红包信息列表
            getRedPackages(pid, unitAmount, userRedPackages, userIdList);
            // 持久化信息至数据库
            count += batchUpdate(userRedPackages);
        }
        redisDao.delete(LIST_KEY_PREFIX + pid);
        Long end = System.currentTimeMillis();
        System.out.println("耗时" + (end - begin) + "毫秒，共" + count + "记录被保存");
        return count;
    }

    /**
     * 生成红包信息列表
     * @param pid
     * @param unitAmount
     * @param userRedPackages
     * @param userIdList
     */
    private void getRedPackages(Integer pid, Double unitAmount, List<UserRedPackageEntity> userRedPackages, List userIdList) {
        userRedPackages.clear();
        for (int j = 0; j < userIdList.size(); j++) {
            String args = userIdList.get(j).toString();
            String[] arr = args.split("-");
            Integer userId = Integer.parseInt(arr[0]);
            Long time = Long.parseLong(arr[1]);
            UserRedPackageEntity userRedPackageEntity = new UserRedPackageEntity();
            userRedPackageEntity.setPid(pid);
            userRedPackageEntity.setCustomerId(userId);
            userRedPackageEntity.setAmount(unitAmount);
            userRedPackageEntity.setGrabDate(new Timestamp(time));
            userRedPackageEntity.setRemark("抢红包" + pid);
            userRedPackages.add(userRedPackageEntity);
        }
    }

    /**
     * 获取用户id集合
     * @param ops
     * @param i
     * @return
     */
    private List getUserIdList(BoundListOperations ops, int i) {
        List userIdList;
        if(i == 0) {
            userIdList = ops.range(i * FETCH_MAX_SIZE, (i + 1) * FETCH_MAX_SIZE);
        }else{
            userIdList = ops.range(i * FETCH_MAX_SIZE + 1, (i + 1) * FETCH_MAX_SIZE);
        }
        return userIdList;
    }

    /**
     * 动态计算大小
     * @param size
     * @return
     */
    private long computeSize(Long size) {
        return size % FETCH_MAX_SIZE == 0 ?
                        size / FETCH_MAX_SIZE :
                            size / FETCH_MAX_SIZE + 1;
    }

    /**
     * 持久化缓存数据
     * @param userRedPackages
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int batchUpdate(List<UserRedPackageEntity> userRedPackages){
        try{
            int count = redPackageDao.batchReduceStock(userRedPackages.stream().map(UserRedPackageEntity::getPid).collect(toList()));
            count += userRedPackageDao.batchInsert(userRedPackages);
            return count;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 抢红包--基于lua脚本
     *
     * @param pid
     * @param customerId
     * @return
     */
    @Override
    public boolean grab(Integer pid, Integer customerId) {
        /*
            基于lua脚本抢红包
            1、获取抢红包时间
            2、执行我们预先编写的lua脚本，返回结果数字
            3、如果红包只剩下一个时，将单价查询出来并添加到redis缓存链表中
         */
        String args = customerId + "-" + System.currentTimeMillis();
        Long result = redisDao.execute(pid + "", args);
        if(result == 2) add(pid, redisDao.getUnitAmount(pid));
        return result > 0;
    }
}
