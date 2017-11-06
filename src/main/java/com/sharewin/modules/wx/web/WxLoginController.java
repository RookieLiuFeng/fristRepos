package com.sharewin.modules.wx.web;

import com.sharewin.common.model.Result;
import com.sharewin.common.utils.IPUtils;
import com.sharewin.common.utils.encode.Encrypt;
import com.sharewin.common.utils.mapper.JsonMapper;
import com.sharewin.common.weixin.SNSUserInfo;
import com.sharewin.common.weixin.WeixinOauth2Token;
import com.sharewin.common.weixin.WxUtils;
import com.sharewin.core.security.SecurityConstants;
import com.sharewin.core.security.SecurityType;
import com.sharewin.core.security.SecurityUtils;
import com.sharewin.core.security.SessionInfo;
import com.sharewin.core.security.annotation.RequiresUser;
import com.sharewin.modules.sys.entity.User;
import com.sharewin.modules.sys.service.UserManager;
import com.sharewin.modules.wx.service.WxDlfmManager;
import com.sharewin.utils.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "${frontPath}/login")
public class WxLoginController {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public UserManager userManager;

    @Autowired
    public WxDlfmManager wxDlfmManager;
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
            String resultUrl = request.getContextPath() + AppConstants.getFrontPath() + "/index";
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
        String resultUrl = request.getContextPath() + AppConstants.getFrontPath();
        return "redirect:"+resultUrl;
    }


    @RequestMapping(value = "oauth")
    @RequiresUser(required = false)
    public String oauth(HttpServletRequest request, HttpServletResponse response, Model model) {
        // 用户同意授权后，能获取到code
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        if (!"authdeny".equals(code)) {
            // 获取网页授权access_token
            WeixinOauth2Token weixinOauth2Token = WxUtils.getOauth2AccessToken(AppConstants.appid, AppConstants.appsecret, code);

            //logger.info("获取网页授权:"+JsonMapper.getInstance().toJson(weixinOauth2Token));
            // 网页授权接口访问凭证
            String accessToken = weixinOauth2Token.getAccess_token();
            // 用户标识
            String openId = weixinOauth2Token.getOpenid();
            // 获取用户信息
            SNSUserInfo snsUserInfo = WxUtils.getSNSUserInfo(accessToken, openId);
            if (snsUserInfo.getErrcode() != null) {
                logger.info("获取用户信息:" + JsonMapper.getInstance().toJson(snsUserInfo));
                request.setAttribute("errormsg", "获取授权信息失败!");
                return "error";
            } else {
                Integer userid = wxDlfmManager.checkBindAccount(snsUserInfo);
                if (userid == null) {
                    model.addAttribute("openid", snsUserInfo.getOpenid());
                    return "modules/wx/binduser";
                } else {
                    SecurityUtils.putUserToSession(request, userManager.getById(userid));
                    model.addAttribute("user", wxDlfmManager.getUsers(userid));
                    return "modules/wx/mycenter";
                }

            }
        }
        request.setAttribute("errormsg", "authdeny错误!");
        return "error";
    }

    @RequestMapping(value = "binduser.json", method = RequestMethod.POST)
    @ResponseBody
    @RequiresUser(required = false)
    public Result bindUser(HttpServletRequest request,String account, String password, String openid) {
        Result result = wxDlfmManager.bindUserAccount(openid, account, password);
        if(result.getCode()==1){
            SecurityUtils.putUserToSession(request, userManager.getById(Integer.parseInt(result.getObj().toString())));
        }
        return result;
    }

    @RequestMapping(value = "binduser")
    @RequiresUser(required = false)
    public String bindUser(String openid,Model model){
        model.addAttribute("openid",openid);
        return "modules/wx/binduser";
    }

}
