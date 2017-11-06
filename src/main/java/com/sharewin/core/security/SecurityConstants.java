package com.sharewin.core.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 项目中用到的静态变量.
 */
public class SecurityConstants {
    /**
     * session 登录用户key
     */
    public static final String SESSION_SESSIONINFO = "sessionInfo";

    /**
     * session 未授权的URL
     */
    public static final String SESSION_UNAUTHORITY_URL = "UNAUTHORITY_URL";

    /**
     * session 未授权的页面
     */
    public static final String SESSION_UNAUTHORITY_PAGE = "/common/403.jsp";
    public static final String SESSION_UNAUTHORITY_LOGIN_PAGE = "/jump.jsp";

  //  public static final String WX_UNAUTHORITY_LOGIN_PAGE = "/jumpwx.jsp";

    public static final String WX_UNAUTHORITY_LOGIN_PAGE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf616ddc274817a00&redirect_uri=http%3a%2f%2ftgwx.share-win.net%2fdlfm%2fwx%2flogin%2foauth&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";

    /**
     * 超级管理员
     */
    public static final String ROLE_SUPERADMIN = "超级管理员";

    /**
     * 在线用户列表.
     */
    public static final Map<String,SessionInfo> sessionInfoMap = new ConcurrentHashMap<String, SessionInfo>();

    /**
     * 安全日志拦截bean名称
     */
    public static final String SERVICE_SECURITY_LOGINASPECT = "securityLogAspect";

}
