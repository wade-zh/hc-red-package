package com.hongwei.demo.service;

public interface IUserRedPackageService {

    /**
     * 抢红包
     * @param pid {@link com.hongwei.demo.entity.RedPackageEntity#pid}
     * @param customerId {@link com.hongwei.demo.entity.UserRedPackageEntity#customerId}
     * @return
     */
    public boolean grabRedPackage(Integer pid, Integer customerId);

    /**
     * 抢红包-悲观锁实现
     * @param pid {@link #grabRedPackage(Integer, Integer)}
     * @param customerId {@link #grabRedPackage(Integer, Integer)}
     * @return
     */
    public boolean grabRedPackagePessimistic(Integer pid, Integer customerId);



    /**
     * 抢红包-乐观锁实现
     * @param pid {@link #grabRedPackage(Integer, Integer)}
     * @param customerId {@link #grabRedPackage(Integer, Integer)}
     * @return
     */
    public boolean grabRedPackageOptimistic(Integer pid, Integer customerId);



    /**
     * 抢红包-基于redis缓存
     * @param pid {@link #grabRedPackage(Integer, Integer)}
     * @param customerId {@link #grabRedPackage(Integer, Integer)}
     * @return
     */
    public boolean grabRedPackageCache(Integer pid, Integer customerId);

}
