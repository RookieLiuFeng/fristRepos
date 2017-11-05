package com.sharewin.modules.sys.service;

import com.sharewin.common.orm.Page;
import com.sharewin.common.orm.entity.StatusState;
import com.sharewin.common.orm.hibernate.EntityManager;
import com.sharewin.common.orm.hibernate.HibernateDao;
import com.sharewin.common.orm.hibernate.Parameter;
import com.sharewin.common.utils.StringUtils;
import com.sharewin.modules.sys.entity.DlfmAct;
import com.sharewin.modules.sys.entity.DlfmActRoot;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ActRootManager extends EntityManager<DlfmActRoot,Integer> {

    private HibernateDao<DlfmActRoot,Integer> actRootDao;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        actRootDao = new HibernateDao<DlfmActRoot, Integer>(sessionFactory,
                DlfmActRoot.class);
    }

    @Override
    protected HibernateDao getEntityDao() {
        return actRootDao;
    }



    /**
     * 活动数据页
     * @param actName
     * @param page
     * @return
     */
    public Page<DlfmActRoot> getActRootByQuery(String actName, Page<DlfmActRoot> page){
        Parameter parameter = new Parameter();
        StringBuilder hql = new StringBuilder();
        hql.append("select u from DlfmActRoot u where u.status=:status ");
        parameter.put("status",StatusState.normal.getValue());
        if(StringUtils.isNotBlank(actName)){
            hql.append("and u.actName like :actName");
            parameter.put("actName","%"+actName+"%");
        }
        //设置分页
        page = actRootDao.findPage(page,hql.toString(),parameter);
        return page;
    }



    /**
     * ======================================================================
     *                               微网页
     * ======================================================================
     */

    @Deprecated
    public List<Map<String,Object>> getWxActList(int page){
        Parameter parameter = new Parameter();
        String sql = "select u.id,u.act_name as actName,u.act_cover_url as actCoverUrl,DATE_FORMAT(u.create_time,'%Y年%m月%d日 %h:%i') as createTime,u.act_time as actTime from dlfm_act u where u.status=1 order by u.create_time desc";
        return getEntityDao().findBySql(sql,parameter,Map.class,page,15);
    }

    public List<Map<String,Object>> getWxActRoot(int pageNo){
        Parameter parameter = new Parameter();
        String sql = "select u.id,u.act_name as actName,u.act_cover_url as actCoverUrl,DATE_FORMAT(u.create_time,'%Y年%m月%d日 %h:%i') as createTime,act_type as actType from dlfm_act_root u where u.status=1 order by u.create_time desc";
        List<Map<String,Object>> actRoots = getEntityDao().findBySql(sql,null,Map.class,pageNo,10);
       if(actRoots!=null){
            String actSql = "select u.id,u.act_name as actName,u.act_cover_url as actCoverUrl,DATE_FORMAT(u.create_time,'%Y年%m月%d日 %h:%i') as createTime from dlfm_act u where u.status=1 and u.act_root_id=:actRootId order by u.create_time desc";
            for(int i=0;i<actRoots.size();i++){
              parameter.clear();
              parameter.put("actRootId",actRoots.get(i).get("id"));
              actRoots.get(i).put("actList",getEntityDao().findBySql(actSql,parameter,Map.class));
            }
        }
       return actRoots;
    }
}
