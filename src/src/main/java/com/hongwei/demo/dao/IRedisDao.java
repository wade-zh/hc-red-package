package com.hongwei.demo.dao;

import org.springframework.data.redis.core.BoundListOperations;
import redis.clients.jedis.Jedis;

/***
 * 基于redis缓存的包装接口
 */
public interface IRedisDao {
    /**
     * 根据key获取范围内的List
     * @param key
     * @return
     */
    public BoundListOperations boundListOps(String key);

    /**
     * 执行lua脚本
     * @param params
     * @return
     */
    public Long execute(String... params);

    /**
     * 获取单价
     * @param pid
     * @return
     */
    Double getUnitAmount(Integer pid);

    /**
     * 删除
     * @param s
     */
    void delete(String s);
}
