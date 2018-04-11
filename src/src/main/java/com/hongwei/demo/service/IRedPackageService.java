package com.hongwei.demo.service;

import com.hongwei.demo.entity.RedPackageEntity;

public interface IRedPackageService {

    /**
     * 获取红包信息
     * @param pid {@link RedPackageEntity#pid}
     * @return
     */
    public RedPackageEntity getRedPackage(Integer pid);

    /**
     * 扣减红包库存
     * @param pid {@link RedPackageEntity#pid}
     * @return
     */
    public int reduceStock(Integer pid);
}
