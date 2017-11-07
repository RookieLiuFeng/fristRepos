package com.sharewin.modules.sys.service;

import com.sharewin.common.model.Result;
import com.sharewin.common.orm.Page;
import com.sharewin.common.orm.hibernate.EntityManager;
import com.sharewin.common.orm.hibernate.HibernateDao;
import com.sharewin.common.orm.hibernate.Parameter;
import com.sharewin.common.utils.StringUtils;
import com.sharewin.common.utils.collections.Collections3;
import com.sharewin.modules.sys.entity.DlfmBookPeriod;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by yomg on 2017/6/26.
 */
@Service
public class BookRecommendManager extends EntityManager<DlfmBookPeriod,Integer> {

    private HibernateDao<DlfmBookPeriod,Integer> bookRecommendDao;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        bookRecommendDao = new HibernateDao<DlfmBookPeriod, Integer>(sessionFactory,
                DlfmBookPeriod.class);
    }

    @Override
    protected HibernateDao<DlfmBookPeriod, Integer> getEntityDao() {
        return bookRecommendDao;
    }

    /**
     * 数据页
     * @param bookRecommendName
     * @param publishType
     * @param page
     * @return
     */
    public Page<DlfmBookPeriod> getBookRecommendByQuery(String bookRecommendName,String publishType, Page<DlfmBookPeriod> page){
        Parameter parameter = new Parameter();
        StringBuilder hql = new StringBuilder();
        hql.append("select u from DlfmBookPeriod u where 1=1 and u.periodType=1");
        if(StringUtils.isNotBlank(bookRecommendName)){
            hql.append(" and u.periodName like :periodName");
            parameter.put("periodName","%"+bookRecommendName+"%");
        }
        if(StringUtils.isNotBlank(publishType)){
            hql.append(" and u.status=:status");
            parameter.put("status",Integer.parseInt(publishType));
        }
        hql.append(" order by u.status desc");
        //设置分页
        page = bookRecommendDao.findPage(page,hql.toString(),parameter);
        return page;
    }

    @Transactional(readOnly = false)
    public Result publish(Integer id) {
        Parameter parameter = new Parameter();
        parameter.put("id",id);
        bookRecommendDao.updateBySql("update dlfm_book_period u set u.status=1 where id=:id",parameter);
        return Result.successResult();
    }


    /*public List<Map<String,Object>> getWxBookList(int page, int periodType){
        Parameter parameter = new Parameter();
        parameter.put("periodType",periodType);
        //String hql = "select u from DlfmBookPeriod u where u.status=1 and u.periodType=:periodType order by u.id desc";
       // List<Map<String,Object>> periodList = getEntityDao().find(hql,parameter);
        String sql  = "select id,period_name as periodName from dlfm_book_period u where u.status=1 and u.period_type=:periodType order by id desc";
        List<Map<String,Object>> periodList = getEntityDao().findBySql(sql,parameter,Map.class,page,3);
        if(Collections3.isNotEmpty(periodList)){
            String bookSql = "select u.id,u.book_name as bookName,u.book_cover_url as bookCoverUrl from dlfm_book u where u.status=1 and u.book_period_id=:periodId order by u.create_time desc";
            for(int i=0;i<periodList.size();i++){
                parameter.clear();
                parameter.put("periodId",periodList.get(i).get("id"));
                List<Map<String,Object>> bookList = getEntityDao().findBySql(bookSql,parameter,Map.class);
                periodList.get(i).put("dlfmBooks",bookList);
            }
        }
        return periodList;
    }*/
}
