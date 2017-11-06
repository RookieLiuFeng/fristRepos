package com.sharewin.modules.sys.service;

import com.sharewin.common.orm.Page;
import com.sharewin.common.orm.entity.StatusState;
import com.sharewin.common.orm.hibernate.EntityManager;
import com.sharewin.common.orm.hibernate.HibernateDao;
import com.sharewin.common.orm.hibernate.Parameter;
import com.sharewin.common.utils.StringUtils;
import com.sharewin.modules.sys.entity.DlfmBook;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class BookManager extends EntityManager<DlfmBook,Integer> {

    private HibernateDao<DlfmBook,Integer> bookDao;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        bookDao = new HibernateDao<DlfmBook, Integer>(sessionFactory,
                DlfmBook.class);
    }

    @Override
    protected HibernateDao getEntityDao() {
        return bookDao;
    }



    /**
     * 书籍数据页
     * @param bookName
     * @param page
     * @return
     */
    public Page<DlfmBook> getBookByQuery(Integer bookPeriodId,String bookName, Page<DlfmBook> page){
        Parameter parameter = new Parameter();
        StringBuilder hql = new StringBuilder();
        hql.append("select u from DlfmBook u where u.dlfmBookPeriod.id=:bookPeriodId and u.status=:status ");
        parameter.put("bookPeriodId",bookPeriodId);
        parameter.put("status",StatusState.normal.getValue());
        if(StringUtils.isNotBlank(bookName)){
            hql.append("and u.bookName like :bookName");
            parameter.put("bookName","%"+bookName+"%");
        }
        //设置分页
        page = bookDao.findPage(page,hql.toString(),parameter);
        return page;
    }



    /**
     * ======================================================================
     *                               微网页
     * ======================================================================
     */

    public List<Map<String,Object>> getWxBookList(int page,int booktype){
        Parameter parameter = new Parameter();
        parameter.put("booktype",booktype);
        String sql = "select u.id,u.book_name as bookName,u.book_cover_url as bookCoverUrl from dlfm_book u where u.status=1 and u.istj=:booktype order by u.create_time desc";
        return getEntityDao().findBySql(sql,parameter,Map.class,page,15);
    }

    @Transactional(readOnly = false)
    public int addBrowseCount(int id){
        Parameter parameter = new Parameter();
        parameter.put("id",id);
        String sql = "update dlfm_book u set u.browse_count=u.browse_count+1 where u.id=:id";
        return getEntityDao().updateBySql(sql,parameter);
    }

}
