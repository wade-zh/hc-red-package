package com.hongwei.demo.service.impl;

import com.hongwei.demo.dao.IRedPackageMapper;
import com.hongwei.demo.entity.RedPackageEntity;
import com.hongwei.demo.service.IRedPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RedPackageServiceImpl implements IRedPackageService {
    private final IRedPackageMapper redPackageMapper;

    @Autowired
    public RedPackageServiceImpl(IRedPackageMapper redPackageMapper) {
        this.redPackageMapper = redPackageMapper;
    }

    /**
     * 获取红包信息
     *
     * @param pid {@link RedPackageEntity#pid}
     * @return
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public RedPackageEntity getRedPackage(Integer pid) {
        return redPackageMapper.select(pid);
    }

    /**
     * 扣减红包库存
     *
     * @param pid {@link RedPackageEntity#pid}
     * @return
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int reduceStock(Integer pid) {
        return redPackageMapper.reduceStock(pid);
    }
}
