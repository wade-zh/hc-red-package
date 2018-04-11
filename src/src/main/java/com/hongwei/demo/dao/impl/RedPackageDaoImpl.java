package com.hongwei.demo.dao.impl;

import com.hongwei.demo.dao.BaseTableDao;
import com.hongwei.demo.dao.IRedPackageDao;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
@Repository
public class RedPackageDaoImpl extends BaseTableDao implements IRedPackageDao {


    public RedPackageDaoImpl() {
        super("provide_id,amount,send_date,sub_total,unit_amount,stock,version,remark", "red_packages");
    }

    /**
     * 批量扣减库存
     *
     * @return
     */
    public int batchReduceStock(List<Integer> list) {
        String sql = String.format("UPDATE %s SET stock = stock -1 WHERE pid = ?",
                getTableName());
        return (int) Arrays.stream(jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, list.get(i));
            }
            @Override
            public int getBatchSize() {
                return list.size();
            }
        })).count();
    }
}
