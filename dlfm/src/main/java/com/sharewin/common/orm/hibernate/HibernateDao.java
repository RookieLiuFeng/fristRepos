package com.sharewin.common.orm.hibernate;

import com.sharewin.common.orm.Page;
import com.sharewin.common.orm.PropertyFilter;
import com.sharewin.common.orm.PropertyFilter.MatchType;
import com.sharewin.common.utils.StringUtils;
import com.sharewin.common.utils.collections.ArrayUtils;
import com.sharewin.common.utils.reflection.ReflectionUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 封装SpringSide扩展功能的Hibernate DAO泛型基类.
 * <p>
 * 扩展功能包括分页查询,按属性过滤条件列表查询. 可在Service层直接使用,也可以扩展泛型DAO子类使用,见两个构造函数的注释.
 *
 * @param <T>  DAO操作的对象类型
 * @param <PK> 主键类型
 */
public class HibernateDao<T, PK extends Serializable> extends
        SimpleHibernateDao<T, PK> {
    /**
     * 用于Dao层子类的构造函数. 通过子类的泛型定义取得对象类型Class. eg. public class UserDao extends
     * HibernateDao<User, Long>{ }
     */
    public HibernateDao() {
        super();
    }

    /**
     * 用于省略Dao层, Service层直接使用通用HibernateDao的构造函数. 在构造函数中定义对象类型Class. eg.
     * HibernateDao<User, Long> userDao = new HibernateDao<User,
     * Long>(sessionFactory, User.class);
     */
    public HibernateDao(final SessionFactory sessionFactory,
                        final Class<T> entityClass) {
        super(sessionFactory, entityClass);
    }

    // -- 分页查询函数 --//

    /**
     * 分页获取全部对象.
     */
    public Page<T> getAll(final Page<T> page) {
        return findPage(page);
    }

    /**
     * 按HQL分页查询.
     *
     * @param page   分页参数. 注意不支持其中的orderBy参数.
     * @param hql    hql语句.
     * @param values 数量可变的查询参数,按顺序绑定.
     * @return 分页查询结果, 附带结果列表及所有查询输入参数.
     */
    @SuppressWarnings("unchecked")
    public Page<T> findPage(final Page<T> page, final String hql,
                            final Object... values) {
        Assert.notNull(page, "page不能为空");

        Query q = createQuery(hql, values);

        if (page.isAutoCount()) {
            long totalCount = countHqlResult(hql, values);
            page.setTotalCount(totalCount);
        }

        setPageParameterToQuery(q, page);

        List result = q.list();
        page.setResult(result);
        return page;
    }

    /**
     * 按HQL分页查询.
     *
     * @param page      分页参数. 注意不支持其中的orderBy参数.
     * @param hql       hql语句.
     * @param parameter 数量可变的查询参数,按顺序绑定.
     * @return 分页查询结果, 附带结果列表及所有查询输入参数.
     */
    @SuppressWarnings("unchecked")
    public Page<T> findPage(final Page<T> page, final String hql,
                            final Parameter parameter) {
        Assert.notNull(page, "page不能为空");

        Query q = createQuery(hql, parameter);

        if (page.isAutoCount()) {
            long totalCount = countHqlResult(hql, parameter);
            page.setTotalCount(totalCount);
        }

        setPageParameterToQuery(q, page);

        List result = q.list();
        page.setResult(result);
        return page;
    }

    /**
     * 按HQL分页查询.
     *
     * @param page   分页参数. 注意不支持其中的orderBy参数.
     * @param hql    hql语句.
     * @param values 命名参数,按名称绑定.
     * @return 分页查询结果, 附带结果列表及所有查询输入参数.
     */
    @SuppressWarnings("unchecked")
    public Page<T> findPage(final Page<T> page, final String hql,
                            final Map<String, ?> values) {
        Assert.notNull(page, "page不能为空");

        Query q = createQuery(hql, values);

        if (page.isAutoCount()) {
            long totalCount = countHqlResult(hql, values);
            page.setTotalCount(totalCount);
        }

        setPageParameterToQuery(q, page);

        List result = q.list();
        page.setResult(result);
        return page;
    }

    /**
     * 按Criteria分页查询.
     *
     * @param page       分页参数.
     * @param criterions 数量可变的Criterion.
     * @return 分页查询结果.附带结果列表及所有查询输入参数.
     */
    @SuppressWarnings("unchecked")
    public Page<T> findPage(final Page<T> page, final Criterion... criterions) {
        Assert.notNull(page, "page不能为空");

        Criteria c = createCriteria(criterions);

        if (page.isAutoCount()) {
            long totalCount = countCriteriaResult(c);
            page.setTotalCount(totalCount);
        }

        setPageParameterToCriteria(c, page);

        List result = c.list();
        page.setResult(result);
        return page;
    }

    /**
     * 设置分页参数到Query对象,辅助函数.
     */
    public Query setPageParameterToQuery(final Query q, final Page<T> page) {
        Assert.isTrue(page.getPageSize() > 0, "Page Size must larger than zero");

        // hibernate的firstResult的序号从0开始
        q.setFirstResult(page.getFirst() - 1);
        q.setMaxResults(page.getPageSize());
        return q;
    }

    /**
     * 设置分页参数到Criteria对象,辅助函数.
     */
    protected Criteria setPageParameterToCriteria(final Criteria c,
                                                  final Page<T> page) {
        Assert.isTrue(page.getPageSize() > 0, "Page Size must larger than zero");

        // hibernate的firstResult的序号从0开始
        c.setFirstResult(page.getFirst() - 1);
        c.setMaxResults(page.getPageSize());

        //设置排序
        super.setPageParameterToCriteria(c, page.getOrderBy(), page.getOrder());
        return c;
    }

    /**
     * 执行count查询获得本次Hql查询所能获得的对象总数.
     * <p>
     * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
     */
    public long countHqlResult(final String hql, final Object... values) {
        String countHql = prepareCountHql(hql);

        try {
            Long count = findUnique(countHql, values);
            return count;
        } catch (Exception e) {
            throw new RuntimeException("hql can't be auto count, hql is:"
                    + countHql, e);
        }
    }

    /**
     * 执行count查询获得本次Hql查询所能获得的对象总数.
     * <p>
     * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
     */
    protected long countHqlResult(final String hql, final Map<String, ?> values) {
        String countHql = prepareCountHql(hql);

        try {
            Long count = findUnique(countHql, values);
            return count;
        } catch (Exception e) {
            throw new RuntimeException("hql can't be auto count, hql is:"
                    + countHql, e);
        }
    }

    /**
     * 执行count查询获得本次Hql查询所能获得的对象总数.
     */
    protected long countHqlResult(final String hql, final Parameter parameter) {
        String countHql = prepareCountHql(hql);
        Long count = 0L;
        try {
//            Long count = (Long)createQuery(countHql, parameter).uniqueResult();

            Query query = createQuery(countHql, parameter);
            List<Object> list = query.list();
            if (list.size() > 0) {
                count = (Long) list.get(0);
            } else {
                count = Long.valueOf(list.size());
            }

            return count;
        } catch (Exception e) {
            throw new RuntimeException("hql can't be auto count, hql is:"
                    + countHql, e);
        }
    }


    private String prepareCountHql(String orgHql) {
        String fromHql = orgHql;
        // select子句与order by子句会影响count查询,进行简单的排除.
        fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
        fromHql = StringUtils.substringBefore(fromHql, "order by");

        String countHql = "select count(*) " + fromHql;
        return countHql;
    }

    /**
     * 执行count查询获得本次Criteria查询所能获得的对象总数.
     */
    @SuppressWarnings("unchecked")
    protected long countCriteriaResult(final Criteria c) {
        CriteriaImpl impl = (CriteriaImpl) c;

        // 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
        Projection projection = impl.getProjection();
        ResultTransformer transformer = impl.getResultTransformer();

        List<CriteriaImpl.OrderEntry> orderEntries = null;
        try {
            orderEntries = (List) ReflectionUtils.getFieldValue(impl,
                    "orderEntries");
            ReflectionUtils
                    .setFieldValue(impl, "orderEntries", new ArrayList());
        } catch (Exception e) {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }

        // 执行Count查询
        Long totalCountObject = (Long) c.setProjection(Projections.rowCount())
                .uniqueResult();
        long totalCount = (totalCountObject != null) ? totalCountObject : 0;

        // 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
        c.setProjection(projection);

        if (projection == null) {
            c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }
        if (transformer != null) {
            c.setResultTransformer(transformer);
        }
        try {
            ReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
        } catch (Exception e) {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }

        return totalCount;
    }

    // -- 属性过滤条件(PropertyFilter)查询函数 --//

    /**
     * 按属性查找对象列表,支持多种匹配方式.
     *
     * @param matchType 匹配方式,目前支持的取值见PropertyFilter的MatcheType enum.
     */
    public List<T> findBy(final String propertyName, final Object value,
                          final MatchType matchType) {
        Criterion criterion = buildCriterion(propertyName, value, matchType);
        return find(criterion);
    }

    /**
     * 按属性过滤条件列表查找对象列表.
     */
    public List<T> find(List<PropertyFilter> filters) {
        Criterion[] criterions = buildCriterionByPropertyFilter(filters);
        return find(criterions);
    }

    /**
     * 按属性过滤条件列表查找对象列表.
     *
     * @param orderBy 排序字段 多个排序字段时用','分隔.
     * @param order   排序方式"asc"、"desc" 中间以","分割
     */
    public List<T> find(final List<PropertyFilter> filters,
                        final String orderBy, final String order) {
        Criterion[] criterions = buildCriterionByPropertyFilter(filters);
        return find(orderBy, order, criterions);
    }

    /**
     * 按属性过滤条件列表分页查找对象.
     */
    public Page<T> findPage(final Page<T> page,
                            final List<PropertyFilter> filters) {
        Criterion[] criterions = buildCriterionByPropertyFilter(filters);
        return findPage(page, criterions);
    }

    /**
     * 按属性条件参数创建Criterion,辅助函数.
     */
    protected Criterion buildCriterion(final String propertyName,
                                       final Object propertyValue, final MatchType matchType) {
        Assert.hasText(propertyName, "propertyName不能为空");
        Criterion criterion = null;
        String value;
        Character ESCAPE = '!';
        // 根据MatchType构造criterion
        switch (matchType) {
            case EQ:
                criterion = Restrictions.eq(propertyName, propertyValue);
                break;
            case NE:
                criterion = Restrictions.ne(propertyName, propertyValue);
                break;
            case LIKE:
                // 过滤特殊字符
                value = (String) propertyValue;
                if ((ESCAPE.toString()).equals(value)) {
                    criterion = Restrictions.like(propertyName,
                            (String) propertyValue, MatchMode.ANYWHERE);
                } else {
                    criterion = new LikeExpression(propertyName,
                            (String) propertyValue, MatchMode.ANYWHERE, ESCAPE,
                            true);
                }
                break;
            case SLIKE:
                // 过滤特殊字符
                value = (String) propertyValue;
                if ((ESCAPE.toString()).equals(value)) {
                    criterion = Restrictions.like(propertyName,
                            (String) propertyValue, MatchMode.START);
                } else {
                    criterion = new LikeExpression(propertyName,
                            (String) propertyValue, MatchMode.START, ESCAPE,
                            true);
                }
                break;
            case ELIKE:
                // 过滤特殊字符
                value = (String) propertyValue;
                if ((ESCAPE.toString()).equals(value)) {
                    criterion = Restrictions.like(propertyName,
                            (String) propertyValue, MatchMode.END);
                } else {
                    criterion = new LikeExpression(propertyName,
                            (String) propertyValue, MatchMode.END, ESCAPE,
                            true);
                }
                break;
            case LE:
                criterion = Restrictions.le(propertyName, propertyValue);
                break;
            case LT:
                criterion = Restrictions.lt(propertyName, propertyValue);
                break;
            case GE:
                criterion = Restrictions.ge(propertyName, propertyValue);
                break;
            case GT:
                criterion = Restrictions.gt(propertyName, propertyValue);
                break;
            case ISNULL:
                criterion = Restrictions.isNull(propertyName);
        }
        return criterion;
    }

    /**
     * 按属性条件列表创建Criterion数组,辅助函数.
     */
    public Criterion[] buildCriterionByPropertyFilter(
            final List<PropertyFilter> filters) {
        List<Criterion> criterionList = new ArrayList<Criterion>();
        for (PropertyFilter filter : filters) {
            if (!filter.hasMultiProperties()) { // 只有一个属性需要比较的情况.
                Criterion criterion = buildCriterion(filter.getPropertyName(),
                        filter.getMatchValue(), filter.getMatchType());
                criterionList.add(criterion);
            } else {// 包含多个属性需要比较的情况,进行or处理.
                Disjunction disjunction = Restrictions.disjunction();
                for (String param : filter.getPropertyNames()) {
                    Criterion criterion = buildCriterion(param,
                            filter.getMatchValue(), filter.getMatchType());
                    disjunction.add(criterion);
                }
                criterionList.add(disjunction);
            }
        }
        return criterionList.toArray(new Criterion[criterionList.size()]);
    }

    /**
     * 判断对象某些属性的值在数据库中是否唯一.
     *
     * @param uniquePropertyNames 在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
     */
    public boolean isUnique(T entity, String uniquePropertyNames) {
        Assert.hasText(uniquePropertyNames);
        Criteria criteria = getSession().createCriteria(entityClass).setProjection(
                Projections.rowCount());
        String[] nameList = uniquePropertyNames.split(",");
        try {
            // 循环加入唯一列
            for (int i = 0; i < nameList.length; i++) {
                criteria.add(Restrictions.eq(nameList[i],
                        PropertyUtils.getProperty(entity, nameList[i])));
            }

            // 以下代码为了如果是update的情况,排除entity自身.

            String idName = getSessionFactory().getClassMetadata(
                    entity.getClass()).getIdentifierPropertyName();
            if (idName != null) {
                // 取得entity的主键值
                Serializable id = (Serializable) PropertyUtils.getProperty(
                        entity, idName);

                // 如果id!=null,说明对象已存在,该操作为update,加入排除自身的判断
                if (id != null)
                    criteria.add(Restrictions.not(Restrictions.eq(idName, id)));
            }
        } catch (Exception e) {
            ReflectionUtils.convertReflectionExceptionToUnchecked(e);
        }
        return ((Number) criteria.uniqueResult()).intValue() == 0;
    }


    // -------------- SQL Query --------------

    /**
     * SQL 分页查询
     *
     * @param page
     * @param sqlString
     * @return
     */
    public <E> Page<E> findBySql(Page<E> page, String sqlString) {
        return findBySql(page, sqlString, null, null);
    }

    /**
     * SQL 分页查询
     *
     * @param page
     * @param sqlString
     * @param parameter
     * @return
     */
    public <E> Page<E> findBySql(Page<E> page, String sqlString, Parameter parameter) {
        return findBySql(page, sqlString, parameter, null);
    }

    /**
     * SQL 分页查询
     *
     * @param page
     * @param sqlString
     * @param resultClass
     * @return
     */
    public <E> Page<E> findBySql(Page<E> page, String sqlString, Class<?> resultClass) {
        return findBySql(page, sqlString, null, resultClass);
    }

    /**
     * SQL 分页查询
     *
     * @param page
     * @param sqlString
     * @param resultClass
     * @param parameter
     * @return
     */
    @SuppressWarnings("unchecked")
    public <E> Page<E> findBySql(Page<E> page, String sqlString, Parameter parameter, Class<?> resultClass) {
        // get count
        String countSqlString = "select count(*) " + super.removeSelect(super.removeOrders(sqlString));
        // page.setCount(Long.valueOf(createSqlQuery(countSqlString, parameter).uniqueResult().toString()));
        Query countQuery = createSqlQuery(countSqlString, parameter);
        List<Object> list = countQuery.list();
        if (list.size() > 0) {
            page.setTotalCount(Long.valueOf(list.get(0).toString()));
        } else {
            page.setTotalCount(list.size());
        }
        if (page.getTotalCount() < 1) {
            return page;
        }
        // order by
        String sql = sqlString;
        if (StringUtils.isNotBlank(page.getOrderBy()) && StringUtils.isNotBlank(page.getOrder())) {
            String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
            String[] orderArray = StringUtils.split(page.getOrder(), ',');

            Assert.isTrue(orderByArray.length == orderArray.length,
                    "分页多重排序参数中,排序字段与排序方向的个数不相等");
            sql += " order by ";
            for (int i = 0; i < orderByArray.length; i++) {
                sql += orderByArray[i] + " " + orderArray[i];
                if (i != orderByArray.length - 1) {
                    sql += ",";
                }
            }
        }


        SQLQuery query = createSqlQuery(sql, parameter);
        query.setFirstResult(page.getFirstResult());
        query.setMaxResults(page.getMaxResults());
        setResultTransformer(query, resultClass);
        page.setResult(query.list());
        return page;
    }

    /**
     * SQL 分页查询 VO
     * @param page
     * @param sqlString
     * @param parameter
     * @param VoMap
     * @param resultClass
     * @param <E>
     * @return
     */
    public <E> Page<E> findBySql(Page<E> page, String sqlString, Parameter parameter, Parameter VoMap,Class<?> resultClass) {
        // get count
        String countSqlString = "select count(*) " + super.removeSelect(super.removeOrders(sqlString));
        // page.setCount(Long.valueOf(createSqlQuery(countSqlString, parameter).uniqueResult().toString()));
        Query countQuery = createSqlQuery(countSqlString, parameter);
        List<Object> list = countQuery.list();
        if (list.size() > 0) {
            page.setTotalCount(Long.valueOf(list.get(0).toString()));
        } else {
            page.setTotalCount(list.size());
        }
        if (page.getTotalCount() < 1) {
            return page;
        }

        // order by
        String sql = sqlString;
        if (StringUtils.isNotBlank(page.getOrderBy()) && StringUtils.isNotBlank(page.getOrder())) {
            String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
            String[] orderArray = StringUtils.split(page.getOrder(), ',');

            Assert.isTrue(orderByArray.length == orderArray.length,
                    "分页多重排序参数中,排序字段与排序方向的个数不相等");
            sql += " order by ";
            for (int i = 0; i < orderByArray.length; i++) {
                sql += orderByArray[i] + " " + orderArray[i];
                if (i != orderByArray.length - 1) {
                    sql += ",";
                }
            }
        }

        SQLQuery query = createSqlQuery(sql, parameter);
        setParameterForScalar(query,VoMap);
        query.setFirstResult(page.getFirstResult());
        query.setMaxResults(page.getMaxResults());
        query.setResultTransformer(Transformers.aliasToBean(resultClass));
        page.setResult(query.list());
        return page;
    }

    /**
     * 设置查询VO属性
     *
     * @param query
     * @param parameter
     */
    protected void setParameterForScalar(SQLQuery query, Parameter parameter) {
        if (parameter != null) {
            Set<String> keySet = parameter.keySet();
            for (String string : keySet) {
                Object value = parameter.get(string);
                query.addScalar(string, (Type) value);
            }
        }
    }


    // -------------- 原生SQL    VO --------------

    /**
     * VO版
     * SQL 分页查询
     *
     * @param page
     * @param sqlString
     * @param resultClass
     * @param parameter
     * @return
     */
    @SuppressWarnings("unchecked")
    public <E> Page<E> findBySqlForVo(Page<E> page, String sqlString, Parameter parameter, Class<?> resultClass) {
        return findBySqlForVo(page, sqlString, parameter, resultClass, null);
    }

    /**
     * VO版
     * SQL 分页查询
     *
     * @param page
     * @param sqlString
     * @param resultClass
     * @param parameter
     * @return
     */
    @SuppressWarnings("unchecked")
    public <E> Page<E> findBySqlForVo(Page<E> page, String sqlString, Parameter parameter, Class<?> resultClass, String[] properties) {
        // get count
        String countSqlString = "select count(*) " + super.removeSelect(super.removeOrders(sqlString));
        Query countQuery = createSqlQuery(countSqlString, parameter);
        List<Object> list = countQuery.list();
        if (list.size() > 0) {
            page.setTotalCount(Long.valueOf(list.get(0).toString()));
        } else {
            page.setTotalCount(list.size());
        }
        if (page.getTotalCount() < 1) {
            return page;
        }
        // order by
        String sql = sqlString;
        if (StringUtils.isNotBlank(page.getOrderBy()) && StringUtils.isNotBlank(page.getOrder())) {
            String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
            String[] orderArray = StringUtils.split(page.getOrder(), ',');

            Assert.isTrue(orderByArray.length == orderArray.length,
                    "分页多重排序参数中,排序字段与排序方向的个数不相等");
            sql += " order by ";
            for (int i = 0; i < orderByArray.length; i++) {
                sql += orderByArray[i] + " " + orderArray[i];
                if (i != orderByArray.length - 1) {
                    sql += ",";
                }
            }
        }
        SQLQuery query = createSqlQuery(sql, parameter);
        setScalarBean(query, resultClass, properties);
        query.setFirstResult(page.getFirstResult());
        query.setMaxResults(page.getMaxResults());
        query.setResultTransformer(Transformers.aliasToBean(resultClass));
        page.setResult(query.list());
        return page;
    }

    /**
     * VO 分页
     * 原生sql 查询列表 按VO 转换
     *
     * @param sqlString
     * @param parameter
     * @param resultClass
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<T> findBySqlForVo(String sqlString, Parameter parameter, Class<?> resultClass, int pageNo, int pageSize) {
        return findBySqlForVo(sqlString, parameter, resultClass, null, pageNo, pageSize);
    }


    /**
     * VO 分页
     * 原生sql 查询列表 按VO 部分属性转换
     *
     * @param sqlString
     * @param parameter
     * @param resultClass
     * @param properties
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<T> findBySqlForVo(String sqlString, Parameter parameter, Class<?> resultClass, String[] properties, int pageNo, int pageSize) {
        SQLQuery query = createSqlQuery(sqlString, parameter);
        setScalarBean(query, resultClass, properties);
        query.setFirstResult((pageNo - 1) * pageSize);
        query.setMaxResults(pageSize);
        query.setResultTransformer(Transformers.aliasToBean(resultClass));
        return query.list();
    }

    /**
     * VO 分页
     * 原生sql 查询列表 VO排除部分属性转换
     *
     * @param sqlString
     * @param parameter
     * @param resultClass
     * @param properties2Exclude
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<T> findBySqlForVoWithExcludeProperties(String sqlString, Parameter parameter, Class<?> resultClass, String[] properties2Exclude, int pageNo, int pageSize) {
        SQLQuery query = createSqlQuery(sqlString, parameter);
        setScalarBeanWithExcludeProperties(query, resultClass, properties2Exclude);
        query.setFirstResult((pageNo - 1) * pageSize);
        query.setMaxResults(pageSize);
        query.setResultTransformer(Transformers.aliasToBean(resultClass));
        return query.list();
    }


    /**
     * VO 原生sql查询列表
     *
     * @param sqlString
     * @param parameter
     * @param resultClass
     * @return
     */
    public List findBySqlForVo(String sqlString, Parameter parameter, Class<?> resultClass) {
        return findBySqlForVo(sqlString, parameter, resultClass, null);
    }

    /**
     * VO 原生sql 查询列表 按VO 部分属性转换
     *
     * @param sqlString
     * @param parameter
     * @param resultClass
     * @param properties
     * @return
     */
    public List<T> findBySqlForVo(String sqlString, Parameter parameter, Class<?> resultClass, String[] properties) {
        SQLQuery query = createSqlQuery(sqlString, parameter);
        setScalarBean(query, resultClass, properties);
        query.setResultTransformer(Transformers.aliasToBean(resultClass));
        return query.list();
    }

    /**
     * VO版
     * 原生sql 查询列表 排除部分属性转换
     *
     * @param sqlString
     * @param parameter
     * @param resultClass
     * @param properties2Exclude
     * @return
     */
    public List findBySqlForVoWithExcludeProperties(String sqlString, Parameter parameter, Class<?> resultClass, String[] properties2Exclude) {
        SQLQuery query = createSqlQuery(sqlString, parameter);
        setScalarBeanWithExcludeProperties(query, resultClass, properties2Exclude);
        query.setResultTransformer(Transformers.aliasToBean(resultClass));
        return query.list();
    }

    /**
     * VO版
     * SQL 分页查询 排除部分属性转换
     *
     * @param page
     * @param sqlString
     * @param resultClass
     * @param parameter
     * @return
     */
    @SuppressWarnings("unchecked")
    public <E> Page<E> findBySqlForVoWithExcludeProperties(Page<E> page, String sqlString, Parameter parameter, Class<?> resultClass, String[] properties2Exclude) {
        // get count
        String countSqlString = "select count(*) " + super.removeSelect(super.removeOrders(sqlString));
        Query countQuery = createSqlQuery(countSqlString, parameter);
        List<Object> list = countQuery.list();
        if (list.size() > 0) {
            page.setTotalCount(Long.valueOf(list.get(0).toString()));
        } else {
            page.setTotalCount(list.size());
        }
        if (page.getTotalCount() < 1) {
            return page;
        }
        // order by
        String sql = sqlString;
        if (StringUtils.isNotBlank(page.getOrderBy()) && StringUtils.isNotBlank(page.getOrder())) {
            String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
            String[] orderArray = StringUtils.split(page.getOrder(), ',');

            Assert.isTrue(orderByArray.length == orderArray.length,
                    "分页多重排序参数中,排序字段与排序方向的个数不相等");
            sql += " order by ";
            for (int i = 0; i < orderByArray.length; i++) {
                sql += orderByArray[i] + " " + orderArray[i];
                if (i != orderByArray.length - 1) {
                    sql += ",";
                }
            }
        }
        SQLQuery query = createSqlQuery(sql, parameter);
        setScalarBeanWithExcludeProperties(query, resultClass, properties2Exclude);
        query.setFirstResult(page.getFirstResult());
        query.setMaxResults(page.getMaxResults());
        setResultTransformer(query, resultClass);
        page.setResult(query.list());
        return page;
    }

    public void setScalarBean(SQLQuery query, Class<?> clss) {
        Field[] fields = clss.getDeclaredFields();
        if (!ArrayUtils.isEmpty(fields)) {
            for (int i = 0; i < fields.length; i++) {
                addFieldScalar(query, fields[i]);
            }
        }
    }

    /**
     * @param query
     * @param clss
     * @param propertyArray
     */
    public void setScalarBean(SQLQuery query, Class<?> clss, String[] propertyArray) {
        if (ArrayUtils.isEmpty(propertyArray)) {
            setScalarBean(query, clss);
        } else {
            Field[] fields = clss.getDeclaredFields();
            if (!ArrayUtils.isEmpty(fields)) {
                HashSet<String> properties = new HashSet(propertyArray.length);
                Collections.addAll(properties, propertyArray);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    if (properties.contains(field.getName())) {
                        addFieldScalar(query, field);
                    }
                }
            }
        }
    }

    /**
     * @param query
     * @param clss
     * @param properties2Exclude
     */
    public void setScalarBeanWithExcludeProperties(SQLQuery query, Class<?> clss, String[] properties2Exclude) {
        if (ArrayUtils.isEmpty(properties2Exclude)) {
            setScalarBean(query, clss);
        } else {
            Field[] fields = clss.getDeclaredFields();
            if (!ArrayUtils.isEmpty(fields)) {
                HashSet<String> properties = new HashSet(properties2Exclude.length);
                Collections.addAll(properties, properties2Exclude);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    if (!properties.contains(field.getName())) {
                        addFieldScalar(query, field);
                    }
                }
            }
        }
    }

    /**
     * 封装部分常用的类型，请按需修改
     *
     * @param query
     * @param field
     */
    public void addFieldScalar(SQLQuery query, Field field) {
        String fieldTypeName = field.getType().getName();
        if (fieldTypeName.equals("java.lang.String")) {
            query.addScalar(field.getName(), StandardBasicTypes.STRING);
        } else if (fieldTypeName.equals("int") || fieldTypeName.equals("java.lang.Integer")) {
            query.addScalar(field.getName(), StandardBasicTypes.INTEGER);
        } else if (fieldTypeName.equals("java.sql.Date") || fieldTypeName.equals("java.util.Date")) {
            query.addScalar(field.getName(), StandardBasicTypes.TIMESTAMP);
        } else if (fieldTypeName.equals("double") || fieldTypeName.equals("java.lang.Double")) {
            query.addScalar(field.getName(), StandardBasicTypes.DOUBLE);
        } else if (fieldTypeName.equals("float") || fieldTypeName.equals("java.lang.Float")) {
            query.addScalar(field.getName(), StandardBasicTypes.FLOAT);
        } else if (fieldTypeName.equals("java.math.BigDecimal")) {
            query.addScalar(field.getName(), StandardBasicTypes.BIG_DECIMAL);
        }
    }


    /**
     * 原生sql 记录条数
     *
     * @param sqlString
     * @param parameter
     * @return
     */
    public int countSqlResult(String sqlString, Parameter parameter) {
        SQLQuery query = createSqlQuery(sqlString, parameter);
        return ((Number) query.uniqueResult()).intValue();
    }

}
