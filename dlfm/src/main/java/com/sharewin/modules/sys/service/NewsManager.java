package com.sharewin.modules.sys.service;

import com.sharewin.common.orm.Page;
import com.sharewin.common.orm.entity.StatusState;
import com.sharewin.common.orm.hibernate.EntityManager;
import com.sharewin.common.orm.hibernate.HibernateDao;
import com.sharewin.common.orm.hibernate.Parameter;
import com.sharewin.common.utils.StringUtils;
import com.sharewin.modules.sys.entity.DlfmNews;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class NewsManager extends EntityManager<DlfmNews,Integer> {

    private HibernateDao<DlfmNews,Integer> newsDao;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        newsDao = new HibernateDao<DlfmNews, Integer>(sessionFactory,
                DlfmNews.class);
    }

    @Override
    protected HibernateDao getEntityDao() {
        return newsDao;
    }


    /**
     * 新闻数据页
     * @param newsName
     * @param page
     * @return
     */
    public Page<DlfmNews> getNewsByQuery(String newsName, Page<DlfmNews> page){
        Parameter parameter = new Parameter();
        StringBuilder hql = new StringBuilder();
        hql.append("select u from DlfmNews u where u.status=:status");
        parameter.put("status",StatusState.normal.getValue());
        if(StringUtils.isNotBlank(newsName)){
            hql.append(" and u.newsName like :newsName");
            parameter.put("newsName","%"+newsName+"%");
        }
        //设置分页
        page = newsDao.findPage(page,hql.toString(),parameter);
        return page;
    }


    /**
     * ======================================================================
     *                               微网页
     * ======================================================================
     */

    public List<Map<String,Object>> getWxNewsList(int page){
        String sql = "select u.id,u.news_name as newsName,u.news_cover_url as newsCover, date_format(u.create_time,'%Y-%m-%d') as createTime from dlfm_news u where u.status=1 order by u.create_time desc";
        return getEntityDao().findBySql(sql,null,Map.class,page,15);
    }

    @Transactional(readOnly = false)
    public int addBrowseCount(int id){
        Parameter parameter = new Parameter();
        parameter.put("id",id);
        String sql = "update dlfm_news u set u.browse_count=u.browse_count+1 where u.id=:id";
        return getEntityDao().updateBySql(sql,parameter);
    }

}
