package com.hongwei.demo.service.impl;

import com.hongwei.demo.dao.IRedPackageDao;
import com.hongwei.demo.dao.IRedisDao;
import com.hongwei.demo.dao.IUserRedPackageDao;
import com.hongwei.demo.service.IRedisRedPackageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisRedPackageServiceImplTest {
    @Autowired
    private IRedisRedPackageService redisRedPackageService;


    @Test
    public void testGrab() {
        redisRedPackageService.grab(1,1);
    }
}