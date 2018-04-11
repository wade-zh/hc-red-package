package com.hongwei.demo.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/***
 * 用户抢红包实体类--支持序列化
 */
public class UserRedPackageEntity implements Serializable {
    private Integer upid;
    private Integer pid;
    private Integer customerId;
    private Double amount;
    private Timestamp grabDate;
    private String remark;

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public Integer getUpid() {
        return upid;
    }

    public void setUpid(Integer upid) {
        this.upid = upid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Timestamp getGrabDate() {
        return grabDate;
    }

    public void setGrabDate(Timestamp grabDate) {
        this.grabDate = grabDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
