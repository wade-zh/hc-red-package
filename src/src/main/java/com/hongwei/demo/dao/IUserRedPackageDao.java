package com.hongwei.demo.dao;


import com.hongwei.demo.entity.UserRedPackageEntity;

import java.util.Hashtable;
import java.util.List;

public interface IUserRedPackageDao {
    /**
     * 批量添加抢红包记录
     * @param list
     * @return
     */
    public int batchInsert(List<UserRedPackageEntity> list);
}
