package com.hongwei.demo.dao.impl;

import com.hongwei.demo.dao.BaseTableDao;
import com.hongwei.demo.dao.IUserRedPackageDao;
import com.hongwei.demo.entity.UserRedPackageEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

@Repository
public class UserRedPackageDaoImpl extends BaseTableDao implements IUserRedPackageDao {
    public UserRedPackageDaoImpl() {
        super("pid,customer_id,amount,grab_date,remark", "user_red_packages");
    }

    /**
     * 批量添加抢红包记录
     *
     * @param list
     * @return
     */
    @Override
    public int batchInsert(List<UserRedPackageEntity> list) {
        String sql = String.format("INSERT INTO %s(%s) VALUES(?,?,?,?,?)",
                getTableName(),
                getFields());
        return (int) Arrays.stream(jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, list.get(i).getPid());
                preparedStatement.setInt(2, list.get(i).getCustomerId());
                preparedStatement.setDouble(3, list.get(i).getAmount());
                preparedStatement.setDate(4, Date.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(list.get(i).getGrabDate())));
                preparedStatement.setString(5, list.get(i).getRemark());
            }
            @Override
            public int getBatchSize() {
                return list.size();
            }
        })).count();
    }
}
