package com.hongwei.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public abstract class BaseCacheDao {
    protected final RedisTemplate redisTemplate;

    @Autowired
    protected BaseCacheDao(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
