package com.hongwei.demo.service.impl;

import com.hongwei.demo.dao.IRedPackageMapper;
import com.hongwei.demo.dao.IUserRedPackageMapper;
import com.hongwei.demo.entity.RedPackageEntity;
import com.hongwei.demo.entity.UserRedPackageEntity;
import com.hongwei.demo.service.IUserRedPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

@Service
public class UserRedPackageServiceImpl implements IUserRedPackageService {
    private final IUserRedPackageMapper userRedPackageMapper;
    private final IRedPackageMapper redPackageMapper;


    @Autowired
    public UserRedPackageServiceImpl(IUserRedPackageMapper userRedPackageMapper, IRedPackageMapper redPackageMapper) {
        this.userRedPackageMapper = userRedPackageMapper;
        this.redPackageMapper = redPackageMapper;
    }



    /**
     * 抢红包
     *
     * @param pid        {@link RedPackageEntity#pid}
     * @param customerId {@link UserRedPackageEntity#customerId}
     * @return
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public boolean grabRedPackage(Integer pid, Integer customerId) {
        /*
            1、查询红包信息
            2、若库存大于零则继续往下执行，否则返回失败
            3、生成抢红包数据并插入数据库
            此种做法会发生由于高并发引起的数据不一致问题，导致超卖问题出现
         */
        RedPackageEntity redPackageEntity = redPackageMapper.select(pid);
        if(redPackageEntity.getStock() <= 0) return false;
        redPackageMapper.reduceStock(pid);
        UserRedPackageEntity userRedPackageEntity = new UserRedPackageEntity();
        userRedPackageEntity.setPid(pid);
        userRedPackageEntity.setCustomerId(customerId);
        userRedPackageEntity.setAmount(redPackageEntity.getUnitAmount());
        userRedPackageEntity.setRemark("抢红包" + pid);
        return userRedPackageMapper.grab(userRedPackageEntity) > 0;
    }

    /**
     * 抢红包-悲观锁实现
     *
     * @param pid        {@link #grabRedPackage(Integer, Integer)}
     * @param customerId {@link #grabRedPackage(Integer, Integer)}
     * @return
     */
    @Override
    public boolean grabRedPackagePessimistic(Integer pid, Integer customerId) {
        RedPackageEntity redPackageEntity = redPackageMapper.selectPessimistic(pid);
        if(redPackageEntity.getStock() <= 0) return false;
        redPackageMapper.reduceStock(pid);
        UserRedPackageEntity userRedPackageEntity = new UserRedPackageEntity();
        userRedPackageEntity.setPid(pid);
        userRedPackageEntity.setCustomerId(customerId);
        userRedPackageEntity.setAmount(redPackageEntity.getUnitAmount());
        userRedPackageEntity.setRemark("抢红包" + pid);
        return userRedPackageMapper.grab(userRedPackageEntity) > 0;
    }

    /**
     * 抢红包-乐观锁实现
     *
     * @param pid        {@link #grabRedPackage(Integer, Integer)}
     * @param customerId {@link #grabRedPackage(Integer, Integer)}
     * @return
     */
    @Override
    public boolean grabRedPackageOptimistic(Integer pid, Integer customerId) {
        for (int i = 0; i < 2; i++) {
            RedPackageEntity redPackageEntity = redPackageMapper.select(pid);
            if(redPackageEntity.getStock() <= 0) return false;
            redPackageMapper.reduceStockOptimistic(pid, redPackageEntity.getVersion());
            UserRedPackageEntity userRedPackageEntity = new UserRedPackageEntity();
            userRedPackageEntity.setPid(pid);
            userRedPackageEntity.setCustomerId(customerId);
            userRedPackageEntity.setAmount(redPackageEntity.getUnitAmount());
            userRedPackageEntity.setRemark("抢红包" + pid);
            return userRedPackageMapper.grab(userRedPackageEntity) > 0;
        }
        return false;
    }

    /**
     * 抢红包-基于redis缓存
     *
     * @param pid        {@link #grabRedPackage(Integer, Integer)}
     * @param customerId {@link #grabRedPackage(Integer, Integer)}
     * @return
     *      0=空库存
     *      1=争抢成功
     *      2=库存告警
     */
    @Override
    public boolean grabRedPackageCache(Integer pid, Integer customerId) {

        return false;
    }
}
