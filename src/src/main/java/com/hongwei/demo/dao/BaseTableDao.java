package com.hongwei.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 抽象仓储类
 */
public abstract class BaseTableDao {
    /**
     * 表字段
     */
    protected String fields = null;

    /**
     * 表名称
     */
    public String tableName = null;

    @Autowired
    public JdbcTemplate jdbcTemplate;

    public String getFields() {
        return fields;
    }

    public String getTableName() {
        return tableName;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BaseTableDao(String fields, String tableName) {
        this.fields = fields;
        this.tableName = tableName;
    }

}
