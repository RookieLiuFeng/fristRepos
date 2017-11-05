package com.sharewin.common.orm;

import com.sharewin.common.utils.SysConstants;

import java.text.MessageFormat;

/**
 * 分页SQL工具类.
 * <br>支持MySQL、Oracle、Postgresql分页查询.
 */
public class PageSqlUtils {

    /**
     * 数据库类型
     */
    public static final String DATABSE_TYPE_MYSQL = "mysql";
    public static final String DATABSE_TYPE_POSTGRE = "postgresql";
    public static final String DATABSE_TYPE_ORACLE = "oracle";
    public static final String DATABSE_TYPE_SQLSERVER = "sqlserver";
    /**
     * 分页SQL
     */
    public static final String MYSQL_SQL = "select * from ( {0}) sel_tab00 limit {1},{2}";         //mysql
    public static final String POSTGRE_SQL = "select * from ( {0}) sel_tab00 limit {2} offset {1}";//postgresql
    public static final String ORACLE_SQL = "select * from (select row_.*,rownum rownum_ from ({0}) row_ where rownum <= {1}) where rownum_>{2}"; //oracle
    public static final String SQLSERVER_SQL = "select * from ( select row_number() over(order by tempColumn) tempRowNumber, * from (select top {1} tempColumn = 0, {0}) t ) tt where tempRowNumber > {2}"; //sqlserver

    /**
     * 按照数据库类型，封装SQL（根据配置文件自动识别）.
     * <br>支持MySQL、Oracle、Postgresql分页查询.
     *
     * @param sql  查询语句 例如:"select * from user "
     * @param page 第几页
     * @param rows 页大小
     * @return
     */
    public static String createPageSql(String sql, int page, int rows) {
        int beginNum = (page - 1) * rows;
        Object[] sqlParam = new Object[3];
        sqlParam[0] = sql;
        sqlParam[1] = beginNum;
        sqlParam[2] = rows;
        String jdbcUrl = SysConstants.getJdbcUrl();
        if (jdbcUrl.indexOf(PageSqlUtils.DATABSE_TYPE_MYSQL) != -1) {
            sql = MessageFormat.format(PageSqlUtils.MYSQL_SQL, sqlParam);
        } else if (jdbcUrl.indexOf(
                PageSqlUtils.DATABSE_TYPE_POSTGRE) != -1) {
            sql = MessageFormat.format(PageSqlUtils.POSTGRE_SQL, sqlParam);
        } else {
            int beginIndex = (page - 1) * rows;
            int endIndex = beginIndex + rows;
            sqlParam[2] = beginIndex;
            sqlParam[1] = endIndex;
            if (jdbcUrl.indexOf(DATABSE_TYPE_ORACLE) != -1) {
                sql = MessageFormat.format(ORACLE_SQL, sqlParam);
            } else if (jdbcUrl.indexOf(DATABSE_TYPE_SQLSERVER) != -1) {
                sqlParam[0] = sql.substring(getAfterSelectInsertPoint(sql));
                sql = MessageFormat.format(SQLSERVER_SQL, sqlParam);
            }
        }
        return sql;
    }

    public static String testSql(String sql,int page,int rows){
        String oracle = "select * from (select row_.*,rownum rownum_ from ({0}) row_ where rownum <= {1}) where rownum_>{2}";
        String oql = MessageFormat.format(oracle,new String[]{sql, rows+"",page+""});
        System.out.println(oql);
        return oql;
    }

    private static int getAfterSelectInsertPoint(String sql) {
        int selectIndex = sql.toLowerCase().indexOf("select");
        int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");
        return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
    }

    public static void main(String[] args) {

    }
}
