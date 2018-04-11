package com.hongwei.demo.dao;

import com.hongwei.demo.entity.RedPackageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IRedPackageMapper {

    /**
     * 查询红包信息
     * @param pid {@link RedPackageEntity#pid}
     * @return
     */
    public RedPackageEntity select(Integer pid);


    /**
     * 查询红包信息--悲观锁
     * @param pid {@link RedPackageEntity#pid}
     * @return
     */
    public RedPackageEntity selectPessimistic(Integer pid);


    /**
     * 扣减红包库存
     * @param pid {@link RedPackageEntity#pid}
     * @return
     */
    public int reduceStock(Integer pid);



    /**
     * 扣减红包库存
     * @param pid {@link RedPackageEntity#pid}
     * @param version {@link RedPackageEntity#version}
     * @return
     */
    public int reduceStockOptimistic(@Param("pid") Integer pid, @Param("version") Integer version);
}
