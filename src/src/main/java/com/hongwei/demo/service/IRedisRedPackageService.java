package com.hongwei.demo.service;

/***
 * 基于redis缓存服务的红包业务接口
 */
public interface IRedisRedPackageService {
    /**
     * 保存红包信息到redis服务
     * @param pid
     * @param unitAmount
     */
    public int add(Integer pid, Double unitAmount);

    /**
     * 抢红包--基于lua脚本
     * @param pid
     * @param customerId
     * @return
     */
    public boolean grab(Integer pid, Integer customerId);
}
