package com.hongwei.demo.dao.impl;

import com.hongwei.demo.dao.BaseCacheDao;
import com.hongwei.demo.dao.IRedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/***
 * 基于redis缓存的包装实现
 */
@Repository
public class RedisDaoImpl extends BaseCacheDao implements IRedisDao {

    protected RedisDaoImpl(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public BoundListOperations boundListOps(String key) {
        return redisTemplate.boundListOps(key);
    }


    /**
     * 执行lua脚本
     *
     * @param params
     * @return
     */
    @Override
    public Long execute(String... params) {
        DefaultRedisScript<Integer> script = new DefaultRedisScript<Integer>();
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource(
                "/script.lua")));
        script.setResultType(Integer.class);
        Object result = redisTemplate.execute(script,
                Collections.singletonList(params[0]), params);
        System.out.println(result);
       return 0L;
    }

    /**
     * 获取单价
     *
     * @param pid
     * @return
     */
    @Override
    public Double getUnitAmount(Integer pid) {
        return (Double) redisTemplate.opsForHash().get("red_packet_" + pid, "unit_amount");
    }

    /**
     * 删除
     *
     * @param s
     */
    @Override
    public void delete(String s) {
        redisTemplate.delete(s);
    }
}
