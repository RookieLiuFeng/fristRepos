package com.sharewin.modules.sys.web;

import com.sharewin.common.model.Result;
import com.sharewin.common.utils.IPUtils;
import com.sharewin.common.utils.encode.Encrypt;
import com.sharewin.common.web.springmvc.SimpleController;
import com.sharewin.core.security.SecurityConstants;
import com.sharewin.core.security.SecurityType;
import com.sharewin.core.security.SecurityUtils;
import com.sharewin.core.security.SessionInfo;
import com.sharewin.core.security.annotation.RequiresUser;
import com.sharewin.modules.sys.entity.User;
import com.sharewin.modules.sys.service.UserManager;
import com.sharewin.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by yomg on 2017/5/3 0003.
 */
@Controller
@RequestMapping(value = "${adminPath}/login")
public class LoginController extends SimpleController {

    @Autowired
    public UserManager userManager;
    /**
     * 登录验证
     *
     * @param loginName 用户名
     * @param password  密码
     * @return
     */
    @RequiresUser(required = false)
    @RequestMapping(value = {"login"})
    @ResponseBody
    public Result login(@RequestParam(required = true) String loginName, @RequestParam(required = true) String password, HttpServletRequest request) {
        Result result = null;
        String msg = null;
        // 获取用户信息
        User user = userManager.getUserByLNP(loginName, Encrypt.md5(password));
        if (user == null) {
            msg = "用户名或密码不正确!";
        }
        if (msg != null) {
            result = new Result(Result.ERROR, msg, null);
        } else {
            if (AppConstants.getIsSecurityOn()) {
                List<SessionInfo> userSessionInfos = SecurityUtils.getSessionUser(loginName);
                if (AppConstants.getUserSessionSize() > 0 && userSessionInfos.size() >= AppConstants.getUserSessionSize()) {
                    result = new Result(Result.ERROR, "已达到用户最大会话登录限制[" + AppConstants.getUserSessionSize() + "，请注销其它登录信息后再试！]", AppConstants.getUserSessionSize());
                    return result;
                }
            }
            //将用户信息放入session中
            SecurityUtils.putUserToSession(request, user);
            logger.info("用户{}登录系统,IP:{}.", user.getLoginName(), IPUtils.getIpAddr(request));

            //设置调整URL 如果session中包含未被授权的URL 则跳转到该页面
            String resultUrl = request.getContextPath() + AppConstants.getAdminPath() + "/index";
            Object unAuthorityUrl = request.getSession().getAttribute(SecurityConstants.SESSION_UNAUTHORITY_URL);
            if (unAuthorityUrl != null) {
                resultUrl = unAuthorityUrl.toString();
                //清空未被授权的URL
                request.getSession().setAttribute(SecurityConstants.SESSION_UNAUTHORITY_URL, null);
            }
            //返回
            result = new Result(Result.SUCCESS, "用户验证通过!", resultUrl);
        }
        return result;
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @RequestMapping(value = {"loginout"})
    public String logout(HttpServletRequest request) {
        SessionInfo sessionInfo = SecurityUtils.getCurrentSessionInfo();
        if (sessionInfo != null) {
            // 退出时清空session中的内容
            String sessionId = request.getSession().getId();
            SecurityUtils.removeUserFromSession(sessionId, false, SecurityType.logout);
            logger.info("用户{}退出系统.", sessionInfo.getLoginName());
        }
        String resultUrl = AppConstants.getAdminPath();
        return "redirect:"+resultUrl;
    }


}
