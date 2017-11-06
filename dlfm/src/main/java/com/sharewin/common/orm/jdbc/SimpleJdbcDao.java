package com.sharewin.common.orm.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SimpleJdbcDao {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Spring  JdbcTemplate
     */
    protected JdbcTemplate jdbcTemplate;

    /**
     * Spring NamedParameterJdbcTemplate
     */
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public SimpleJdbcDao() {

    }

    /**
     * 构造方法
     *
     * @param dataSource 数据源
     */
    public SimpleJdbcDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


    /**
     * 根据sql语句，返回Map对象,对于某些项目来说，没有准备Bean对象，则可以使用Map代替Key为字段名,value为值
     *
     * @param sql        语句(参数用冒号加参数名，例如select count(*) from tb where id=:id)
     * @param parameters 参数集合(key为参数名，value为参数值)
     * @return bean对象
     */
    public Map<String, Object> queryForMap(final String sql, Map parameters) {
        Assert.hasText(sql, "sql语句不正确!");
        if (parameters != null) {
            return namedParameterJdbcTemplate.queryForMap(sql, parameters);
        } else {
            return jdbcTemplate.queryForMap(sql);
        }
    }

    /**
     * @param sql  语句(参数用冒号加参数名，例如select count(*) from tb where id=:id)
     * @param bean 参数实体
     * @return
     */
    public Map<String, Object> queryForMap(final String sql, Object bean) {
        Assert.hasText(sql, "sql语句不正确!");
        if (bean != null) {
            return namedParameterJdbcTemplate.queryForMap(sql, paramBeanMapper(bean));
        } else {
            return jdbcTemplate.queryForMap(sql);
        }
    }

    /**
     * @param sql   语句(参数用冒号加参数名，例如select * from tb where id=?)
     * @param param 参数
     * @return
     */
    public Map<String, Object> queryForMap(final String sql, Object[] param) {
        Assert.hasText(sql, "sql语句不正确!");
        if (param != null) {
            return jdbcTemplate.queryForMap(sql, param);
        } else {
            return jdbcTemplate.queryForMap(sql);
        }
    }

    /**
     * 根据sql语句，返回对象
     *
     * @param sql        语句(参数用冒号加参数名，例如select * from tb where id=:id)
     * @param parameters 参数集合(key为参数名，value为参数值)
     * @param clazz      对象
     * @return
     */
    public Object queryForObject(final String sql, Map parameters, Class clazz) {
        Assert.hasText(sql, "sql语句不正确!");
        Assert.notNull(clazz, "对象类型不能为空!");
        try{
            if (parameters != null) {
                return namedParameterJdbcTemplate.queryForObject(sql, parameters, resultBeanMapper(clazz));
            } else {
                return jdbcTemplate.queryForObject(sql, resultBeanMapper(clazz));
            }
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    /**
     * 根据sql语句，返回对象
     *
     * @param sql   语句(参数用冒号加参数名，例如select * from tb where id=:id)
     * @param bean  参数实体
     * @param clazz 对象
     * @return
     */
    public Object queryForObject(final String sql, Object bean, Class clazz) {
        Assert.hasText(sql, "sql语句不正确!");
        Assert.notNull(clazz, "对象类型不能为空!");
        if (bean != null) {
            return namedParameterJdbcTemplate.queryForObject(sql, paramBeanMapper(bean), resultBeanMapper(clazz));
        } else {
            return jdbcTemplate.queryForObject(sql, resultBeanMapper(clazz));
        }
    }

    /**
     * 返回基础类型对象
     *
     * @param sql          SQL语句
     * @param requiredType 基础数据类型
     * @param param        参数
     * @return
     */
    public Object queryForTypeObject(String sql, Class requiredType, Object... param) {
        Assert.hasText(sql, "sql语句不正确!");
        Assert.notNull(requiredType, "requiredType 不能为空");

        if (param != null) {
            return jdbcTemplate.queryForObject(sql, param, requiredType);
        } else {
            return jdbcTemplate.queryForObject(sql, requiredType);
        }
    }

    /**
     * 返回基础类型对象
     *
     * @param sql          SQL语句
     * @param parameters   参数键值对
     * @param requiredType 基础数据类型
     * @return
     */
    public Object queryForTypeObject(String sql, Map parameters, Class requiredType) {
        Assert.hasText(sql, "sql语句不正确!");
        Assert.notNull(requiredType, "requiredType 不能为空");
        try{
            if (parameters != null) {
                return namedParameterJdbcTemplate.queryForObject(sql, parameters, requiredType);
            } else {
                return jdbcTemplate.queryForObject(sql, requiredType);
            }
        }catch (EmptyResultDataAccessException e){
            return null;
        }


    }

    /**
     * 返回基础类型对象
     *
     * @param sql          SQL语句
     * @param bean         参数实体
     * @param requiredType 基础数据类型
     * @return
     */
    public Object queryForTypeObject(String sql, Object bean, Class requiredType) {
        Assert.hasText(sql, "sql语句不正确!");
        Assert.notNull(requiredType, "requiredType 不能为空");
        if (bean != null) {
            return namedParameterJdbcTemplate.queryForObject(sql, paramBeanMapper(bean), requiredType);
        } else {
            return jdbcTemplate.queryForObject(sql, requiredType);
        }

    }


    /**
     * 根据sql语句，返回对象集合
     *
     * @param sql   语句(参数用冒号加参数名，例如select * from tb
     * @param clazz 返回的集合类型
     * @return
     */
    public List queryForList(final String sql, Class clazz) {
        Assert.hasText(sql, "sql语句不正确!");
        return jdbcTemplate.query(sql, resultBeanMapper(clazz));
    }


    /**
     * 根据sql语句，返回对象集合
     *
     * @param sql        语句(参数用冒号加参数名，例如select * from tb where id=:id)
     * @param clazz      类型
     * @param parameters 参数集合(key为参数名，value为参数值)
     * @return
     */
    public List queryForList(final String sql, Map parameters, Class clazz) {
        Assert.hasText(sql, "sql语句不正确!");
        Assert.notNull(clazz, "集合中对象类型不能为空!");
        if (parameters != null) {
            return namedParameterJdbcTemplate.query(sql, parameters, resultBeanMapper(clazz));
        } else {
            return jdbcTemplate.query(sql, resultBeanMapper(clazz));
        }
    }

    /**
     * 根据sql语句，返回对象集合
     *
     * @param sql   语句(参数用冒号加参数名，例如select * from tb where id=:id)
     * @param clazz 类型
     * @param bean  参数实体
     * @return List 对象集合
     */
    public List queryForList(final String sql, Object bean, Class clazz) {
        Assert.hasText(sql, "sql语句不正确!");
        Assert.notNull(clazz, "集合中对象类型不能为空!");
        if (bean != null) {
            return namedParameterJdbcTemplate.query(sql, paramBeanMapper(bean), resultBeanMapper(clazz));
        } else {
            return jdbcTemplate.query(sql, resultBeanMapper(clazz));
        }
    }


    /**
     * 使用指定的检索标准检索数据并返回数据
     *
     * @param sql        SQL语句
     * @param parameters 参数
     */
    public List<Map<String, Object>> queryForList(String sql, Object[] parameters) {
        Assert.hasText(sql, "sql语句不正确!");
        if (parameters != null) {
            return jdbcTemplate.queryForList(sql, parameters);
        } else {
            return jdbcTemplate.queryForList(sql);
        }
    }

    /**
     * 根据sql语句，返回Map对象集合
     *
     * @param sql        语句(参数用冒号加参数名，例如select count(*) from tb where id=:id)
     * @param parameters 参数集合(key为参数名，value为参数值)
     * @return bean对象
     */
    public List<Map<String, Object>> queryForList(final String sql, Map parameters) {
        Assert.hasText(sql, "sql语句不正确!");
        if (parameters != null) {
            return namedParameterJdbcTemplate.queryForList(sql, parameters);
        } else {
            return jdbcTemplate.queryForList(sql);
        }
    }

    /**
     * 根据sql语句，返回Map对象集合
     *
     * @param sql  语句(参数用冒号加参数名，例如select count(*) from tb where id=:id)
     * @param bean 参数实体
     * @return
     */
    public List<Map<String, Object>> queryForList(final String sql, Object bean) {
        Assert.hasText(sql, "sql语句不正确!");
        if (bean != null) {
            return namedParameterJdbcTemplate.queryForList(sql, paramBeanMapper(bean));
        } else {
            return jdbcTemplate.queryForList(sql);
        }
    }


    /**
     * 执行insert，update，delete等操作<br>
     * 例如insert into users (name,login_name,password) values(:name,:loginName,:password)<br>
     * 参数用冒号,参数为bean的属性名
     *
     * @param sql
     * @param bean
     */
    public int executeForObject(final String sql, Object bean) {
        Assert.hasText(sql, "sql语句不正确!");
        if (bean != null) {
            return namedParameterJdbcTemplate.update(sql, paramBeanMapper(bean));
        } else {
            return jdbcTemplate.update(sql);
        }
    }

    /**
     * 执行insert，update，delete等操作<br>
     * 例如insert into users (name,login_name,password) values(:name,:login_name,:password)<br>
     * 参数用冒号,参数为Map的key名
     *
     * @param sql
     * @param parameters
     */
    public int update(final String sql, Map parameters) {
        Assert.hasText(sql, "sql语句不正确!");
        if (parameters != null) {
            return namedParameterJdbcTemplate.update(sql, parameters);
        } else {
            return jdbcTemplate.update(sql);
        }
    }


    /**
     * update更新
     *
     * @param sql   sql语句
     * @param param 参数
     * @return
     */
    public int update(String sql, Object[] param) {
        Assert.hasText(sql, "sql语句不正确!");
        if (param != null) {
            return jdbcTemplate.update(sql, param);
        } else {
            return jdbcTemplate.update(sql);
        }
    }

    /*
     * 批量处理操作
     * 例如：update t_actor set first_name = :firstName, last_name = :lastName where id = :id
     * 参数用冒号
     */
    public int[] batchUpdate(final String sql, List<Object[]> batch) {
        Assert.hasText(sql, "sql语句不正确!");
        int[] updateCounts = jdbcTemplate.batchUpdate(sql, batch);
        return updateCounts;
    }

    /*
    * 批量处理操作
    * 例如：update t_actor set first_name = :firstName, last_name = :lastName where id = :id
    * 参数用冒号
    */
    public int[] batchUpdateByNpjt(final String sql, Map<String, ?>[] batchValues) {
        Assert.hasText(sql, "sql语句不正确!");
        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql, batchValues);
        return updateCounts;
    }


    /*
    * 批量处理操作
    * 例如：update t_actor set first_name = :firstName, last_name = :lastName where id = :id
    * 参数用冒号
    */
    public int[] batchUpdateByNpjt(final String sql, List<Object> batchValues) {
        Assert.hasText(sql, "sql语句不正确!");
        Assert.notNull(batchValues, "batchValues 为空");
        BeanPropertySqlParameterSource[] parameterSources = new BeanPropertySqlParameterSource[batchValues.size()];
        for (int i = 0; i < batchValues.size(); i++) {
            parameterSources[i] = paramBeanMapper(batchValues.get(i));
        }
        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(sql, parameterSources);
        return updateCounts;
    }


    public int insert(String tablename, Map<String, Object> paramMap) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(tablename);
        return simpleJdbcInsert.execute(paramMap);
    }

    public int insert(String tablename,Object paramBean){
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(tablename);
        return simpleJdbcInsert.execute(paramBeanMapper(paramBean));
    }

    public Number insertAndReturnKey(String tablename,String generatedKeyName,Map<String, Object> paramMap){
        SimpleJdbcInsert simpleJdbcInsert =  new SimpleJdbcInsert(jdbcTemplate).withTableName(tablename);
        simpleJdbcInsert.setGeneratedKeyName(generatedKeyName);
        return simpleJdbcInsert.executeAndReturnKey(paramMap);
    }

    public Number insertAndReturnKey(String tablename,String generatedKeyName,Object paramBean){
        SimpleJdbcInsert simpleJdbcInsert =  new SimpleJdbcInsert(jdbcTemplate).withTableName(tablename);
        simpleJdbcInsert.setGeneratedKeyName(generatedKeyName);
        return simpleJdbcInsert.executeAndReturnKey(paramBeanMapper(paramBean));
    }

    public int[] insertBatch(String tablename,Map[] batchMap){
        Assert.notNull(batchMap,"batchBean 为空");
        SimpleJdbcInsert simpleJdbcInsert =  new SimpleJdbcInsert(jdbcTemplate).withTableName(tablename);
        return simpleJdbcInsert.executeBatch(batchMap);
    }

    public int[] insertBatch(String tablename,List<Object> batchBean){
        Assert.notNull(batchBean,"batchBean 为空");
        SimpleJdbcInsert simpleJdbcInsert =  new SimpleJdbcInsert(jdbcTemplate).withTableName(tablename);
        BeanPropertySqlParameterSource[] parameterSources = new BeanPropertySqlParameterSource[batchBean.size()];
        for (int i = 0; i < batchBean.size(); i++) {
            parameterSources[i] = paramBeanMapper(batchBean.get(i));
        }
        return simpleJdbcInsert.executeBatch(parameterSources);
    }

    /**
     * 返回增加数据的主键
     * 例如insert into users (userid,name,login_name,password) values(seq_users.nextval,:name,:login_name,:password)<br>
     *
     * @param sql            sql 语句
     * @param bean
     * @param keyColumnNames 返回的主键列
     * @return
     */
    public int insertForObjectReturnPk(final String sql, Object bean, String[] keyColumnNames) {
        Assert.hasText(sql, "sql语句不正确!");
        Assert.notNull(bean, "bean 不能为空");
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, paramBeanMapper(bean), keyHolder, keyColumnNames);
        return keyHolder.getKey().intValue();
    }


    /**
     * 返回增加数据的主键
     * 例如insert into users (userid,name,login_name,password) values(seq_users.nextval,:name,:login_name,:password)<br>
     *
     * @param sql            sql 语句
     * @param paramMap
     * @param keyColumnNames 返回的主键列
     * @return
     */
    public int insertForMapReturnPk(final String sql, Map<String,Object> paramMap, String[] keyColumnNames) {
        Assert.hasText(sql, "sql语句不正确!");
        Assert.notNull(paramMap, "paramMap 不能为空");
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, paramBeanMapper(paramMap), keyHolder, keyColumnNames);
        return keyHolder.getKey().intValue();
    }



    protected BeanPropertyRowMapper resultBeanMapper(Class clazz) {
        return BeanPropertyRowMapper.newInstance(clazz);
    }

    protected BeanPropertySqlParameterSource paramBeanMapper(Object object) {
        return new BeanPropertySqlParameterSource(object);
    }

    protected MapSqlParameterSource paramBeanMapper(Map<String,Object> map){
        return new MapSqlParameterSource(map);
    }
}
