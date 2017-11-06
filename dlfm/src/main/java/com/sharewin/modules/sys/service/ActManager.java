package com.sharewin.modules.sys.service;

import com.sharewin.common.orm.Page;
import com.sharewin.common.orm.entity.StatusState;
import com.sharewin.common.orm.hibernate.EntityManager;
import com.sharewin.common.orm.hibernate.HibernateDao;
import com.sharewin.common.orm.hibernate.Parameter;
import com.sharewin.common.utils.StringUtils;
import com.sharewin.modules.sys.entity.DlfmAct;
import com.sharewin.modules.sys.entity.DlfmBook;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ActManager extends EntityManager<DlfmAct,Integer> {

    private HibernateDao<DlfmAct,Integer> actDao;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        actDao = new HibernateDao<DlfmAct, Integer>(sessionFactory,
                DlfmAct.class);
    }

    @Override
    protected HibernateDao getEntityDao() {
        return actDao;
    }



    /**
     * 活动数据页
     * @param actName
     * @param page
     * @return
     */
    public Page<DlfmAct> getActByQuery(Integer actRootId,String actName, Page<DlfmAct> page){
        Parameter parameter = new Parameter();
        StringBuilder hql = new StringBuilder();
        hql.append("select u from DlfmAct u where u.status=:status and u.dlfmActRoot.id=:actRootId");
        parameter.put("status",StatusState.normal.getValue());
        parameter.put("actRootId",actRootId);
        if(StringUtils.isNotBlank(actName)){
            hql.append(" and u.actName like :actName");
            parameter.put("actName","%"+actName+"%");
        }
        //设置分页
        page = actDao.findPage(page,hql.toString(),parameter);
        return page;
    }



    /**
     * ======================================================================
     *                               微网页
     * ======================================================================
     */

    public List<Map<String,Object>> getWxActList(int page){
        Parameter parameter = new Parameter();
        String sql = "select u.id,u.act_name as actName,u.act_cover_url as actCoverUrl,DATE_FORMAT(u.create_time,'%Y年%m月%d日 %h:%i') as createTime,u.act_time as actTime from dlfm_act u where u.status=1 order by u.create_time desc";
        return getEntityDao().findBySql(sql,parameter,Map.class,page,15);
    }

}
