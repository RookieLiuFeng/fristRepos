package com.sharewin.modules.sys.service;

import com.sharewin.common.orm.entity.StatusState;
import com.sharewin.common.orm.hibernate.EntityManager;
import com.sharewin.common.orm.hibernate.HibernateDao;
import com.sharewin.common.orm.hibernate.Parameter;
import com.sharewin.common.utils.StringUtils;
import com.sharewin.modules.sys.entity.DlfmBanner;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerManager extends EntityManager<DlfmBanner,Integer> {

    private HibernateDao<DlfmBanner,Integer> bannerDao;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        bannerDao = new HibernateDao<DlfmBanner, Integer>(sessionFactory,
                DlfmBanner.class);
    }

    @Override
    protected HibernateDao getEntityDao() {
        return bannerDao;
    }



    public List<DlfmBanner> getBannerByQuery(String bannerDesc){
        Parameter parameter = new Parameter();
        StringBuilder hql = new StringBuilder();
        hql.append("select u from DlfmBanner u where u.status=:status");
        parameter.put("status",StatusState.normal.getValue());
        if(StringUtils.isNotBlank(bannerDesc)){
            hql.append(" and u.bannerDesc like :bannerDesc");
            parameter.put("bannerDesc","%"+bannerDesc+"%");
        }
        return bannerDao.find(hql.toString(),parameter);
    }

}
