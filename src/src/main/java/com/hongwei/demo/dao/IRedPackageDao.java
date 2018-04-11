package com.hongwei.demo.dao;

import com.hongwei.demo.entity.RedPackageEntity;

import java.util.Hashtable;
import java.util.List;

/***
 * 红包数据接口
 */
public interface IRedPackageDao {
    /**
     * 批量扣减库存
     * @param list
     * @return
     */
    public int batchReduceStock(List<Integer> list);
}
