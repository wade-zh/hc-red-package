package com.hongwei.demo.dao;

import com.hongwei.demo.entity.UserRedPackageEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserRedPackageMapper {

    /**
     * 抢红包
     * @param userRedPackageEntity {@link UserRedPackageEntity}
     * @return
     */
    public int grab(UserRedPackageEntity userRedPackageEntity);


}
