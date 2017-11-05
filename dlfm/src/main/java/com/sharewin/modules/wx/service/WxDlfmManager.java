package com.sharewin.modules.wx.service;

import com.sharewin.common.model.Result;
import com.sharewin.common.orm.jdbc.JdbcDao;
import com.sharewin.common.utils.collections.Collections3;
import com.sharewin.common.utils.encode.Encrypt;
import com.sharewin.common.weixin.SNSUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class WxDlfmManager {

    @Autowired
    private JdbcDao dao;

    @Transactional(readOnly = false)
    public Integer checkBindAccount(SNSUserInfo snsUserInfo) {
        List<Map<String, Object>> wxuser = dao.queryForList("select * from wx_users where openid=:openid", snsUserInfo);
        if (Collections3.isEmpty(wxuser)) {
            dao.executeForObject("insert into wx_users (openid,sex,country,province,city,headimgurl) values (:openid,:sex,:country,:province,:city,:headimgurl)", snsUserInfo);
        }
        Integer userid = (Integer) dao.queryForTypeObject("select userid from wx_users where openid=:openid", snsUserInfo, Integer.class);
        return userid;
    }

    @Transactional(readOnly = false)
    public Result bindUserAccount(String openid, String account, String password) {
        Result result = null;
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("account", account);
        parameters.put("password", Encrypt.md5(password));
        String userSql = "select id from user where login_name=:account and password=:password and status=1";
        Integer userid = (Integer) dao.queryForTypeObject(userSql, parameters, Integer.class);
        if (userid != null) {
            parameters.clear();
            parameters.put("userid", userid);
            parameters.put("openid", openid);
            dao.update("update wx_users set userid=:userid where openid=:openid", parameters);
            result = Result.successResult();
            result.setObj(userid);
        } else {
            result = Result.errorResult();
            result.setMsg("帐号或者密码错误!");
        }

        return result;
    }

    /**
     * 获取用户
     */
    public Map<String, Object> getUsers(Integer userid) {
        Map<String, Object> paratemers = new HashMap<String, Object>();
        paratemers.put("userid", userid);
        String userSql = "select u.id,u.realname,w.headimgurl from user u inner join wx_users w on" +
                " u.id=w.userid where u.id=:userid and u.status=1";
        List<Map<String, Object>> users = dao.queryForList(userSql, paratemers);
        if (Collections3.isNotEmpty(users)) {
            return users.get(0);
        }
        return null;
    }
}
