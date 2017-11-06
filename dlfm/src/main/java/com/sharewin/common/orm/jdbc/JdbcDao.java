package com.sharewin.common.orm.jdbc;

import com.sharewin.common.orm.PageSqlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Spring Jdbc工具类.
 * <br>支持MySQL、Oracle、postgresql、SQL Server分页查询.
 */
@Repository
public class JdbcDao extends SimpleJdbcDao {

    public JdbcDao() {

    }

    @Autowired
    public JdbcDao(@Qualifier("dataSource") DataSource dataSource) {
        super(dataSource);
    }


    /**
     * 使用指定的检索标准检索数据并分页返回数据-采用预处理方式.<br/>
     * 支持MySQL、Oracle、postgresql、SQL Server分页查询. <br/>
     * 例如：
     * @param sql  SQL语句
     * @param page 第几页
     * @param rows 页大小
     * @return
     */
    public List<Map<String, Object>> queryForList(String sql, int page, int rows, Object[] param) {
        Assert.hasText(sql, "sql语句不正确!");
        //封装分页SQL
        sql = PageSqlUtils.createPageSql(sql, page, rows);
        if (param == null) {
            return jdbcTemplate.queryForList(sql);
        }
        return jdbcTemplate.queryForList(sql, param);
    }


}
