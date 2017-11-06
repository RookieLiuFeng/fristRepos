package com.sharewin.modules.sys.service;

import com.google.common.collect.Lists;
import com.sharewin.common.excel.ExcelUtil;
import com.sharewin.common.exception.DaoException;
import com.sharewin.common.exception.ServiceException;
import com.sharewin.common.exception.SystemException;
import com.sharewin.common.model.Result;
import com.sharewin.common.orm.Page;
import com.sharewin.common.orm.entity.StatusState;
import com.sharewin.common.orm.hibernate.EntityManager;
import com.sharewin.common.orm.hibernate.HibernateDao;
import com.sharewin.common.orm.hibernate.Parameter;
import com.sharewin.common.utils.DateUtils;
import com.sharewin.common.utils.StringUtils;
import com.sharewin.common.utils.collections.Collections3;
import com.sharewin.common.utils.encode.Encrypt;
import com.sharewin.modules.sys.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Transient;
import java.io.IOException;
import java.util.List;

@Service
public class UserManager extends EntityManager<User,Integer> {

    private HibernateDao<User,Integer> userDao;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        userDao = new HibernateDao<User, Integer>(sessionFactory,
                User.class);
    }

    @Override
    protected HibernateDao getEntityDao() {
        return userDao;
    }

    @SuppressWarnings("unchecked")
    public User getUserByLNP(String loginName, String password)
            throws DaoException,SystemException,ServiceException {
        Assert.notNull(loginName, "参数[loginName]为空!");
        Assert.notNull(password, "参数[password]为空!");
        Parameter parameter = new Parameter(loginName, password);
        List<User> list = getEntityDao().find(
                "from User u where u.loginName = :p1 and u.password = :p2 and u.status=1",
                parameter);
        return list.isEmpty() ? null : list.get(0);
    }


    /**
     * 用户数据页
     * @param realname
     * @param page
     * @return
     */
    public Page<User> getUserByQuery(String realname,String userType, Page<User> page){
        Parameter parameter = new Parameter();
        StringBuilder hql = new StringBuilder();
        hql.append("select u from User u where u.status=:status ");
        parameter.put("status",StatusState.normal.getValue());
        if(StringUtils.isNotBlank(realname)){
            hql.append(" and u.realname like :realname");
            parameter.put("realname","%"+realname+"%");
        }
        if(userType!=null&&(!userType.equals(""))){
            hql.append(" and userType=:userType");
            parameter.put("userType",Integer.parseInt(userType));
        }
        //设置分页
        page = userDao.findPage(page,hql.toString(),parameter);
        return page;
    }

    public List<User> getUsers(){
        Parameter parameter = new Parameter();
        String hql = "select u from User u where u.status=:status order by u.id";
        parameter.put("status",StatusState.normal.getValue());
        return getEntityDao().find(hql,parameter);
    }

    @Transactional(readOnly = false)
    public Result importExcel(MultipartFile file){
        Result result = null;
        List<User> users = null;
        //List<User> users_new = Lists.newArrayList();
        try {
            if (file != null) {

                users = (List<User>) ExcelUtil.importExcelByIs(file.getInputStream(), User.class);
                if(Collections3.isNotEmpty(users)){
                    for(User user :users){
                        user.setCreateTime(DateUtils.getSysTimestamp());
                        user.setPassword(Encrypt.md5("123456"));
                    }
                }
                userDao.saveOrUpdate(users);
                result = new Result(Result.SUCCESS, "已导入" + users.size() + "条数据.", null);

            } else {
                result = new Result(Result.WARN, "未上传任何文件.", null);
            }

        } catch (IOException e) {
            logger.error("文件导入失败 "+e.getMessage(),e);
            result = new Result(Result.ERROR, "文件导入失败", null);
        } catch (Exception e) {
            logger.error("文件格式不正确，导入失败 "+e.getMessage(),e);
            result = new Result(Result.ERROR, "文件格式不正确，导入失败", null);
        } finally {
            return result;
        }
    }

    @Transactional(readOnly = false)
    public Result deleteUsers(List<Integer> userids){
        String ids = Collections3.convertToString(userids,",");
        String insertSql = "insert into user_del (id,login_name,password,realname,idcardno,enter_school_time,telphone,guardian_name,sex,birthday,create_time,user_type,del_time)" +
                " (select id,login_name,password,realname,idcardno,enter_school_time,telphone,guardian_name,sex,birthday,create_time,user_type,now() from user where id in ("+ids+"))";
        String sql = "delete from user where id in ("+ids+")";
        userDao.updateBySql(insertSql,null);
        userDao.updateBySql(sql,null);
        return Result.successResult();
    }

}
