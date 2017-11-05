package com.sharewin.core.security.interceptor;

import com.google.common.collect.Lists;
import com.sharewin.common.utils.StringUtils;
import com.sharewin.common.utils.collections.Collections3;
import com.sharewin.core.security.SecurityConstants;
import com.sharewin.core.security.SecurityUtils;
import com.sharewin.core.security.SessionInfo;
import com.sharewin.core.security.annotation.RequiresUser;
import com.sharewin.core.security.annotation.WxSite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.List;


/**
 * 权限拦截器
 * 优先级：注解>数据库权限配置
 */
public class AuthorityInterceptor extends HandlerInterceptorAdapter {


    protected Logger logger = LoggerFactory.getLogger(getClass());

    private List<String> excludeUrls = Lists.newArrayList();// 不需要拦截的资源

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //登录用户
        SessionInfo sessionInfo = SecurityUtils.getCurrentSessionInfo();
        String requestUrl = request.getRequestURI();
        //注解处理
        Boolean annotationHandler = this.annotationHandler(request, response, o, sessionInfo, requestUrl);
        if (annotationHandler != null && annotationHandler == true) {
            return annotationHandler;
        }
        //数据库处理
        return this.dbHandler(request, response, o, sessionInfo, requestUrl);
    }

    /**
     * 注解处理
     *
     * @param request
     * @param response
     * @param o
     * @param sessionInfo
     * @param requestUrl
     * @return
     * @throws Exception
     */
    private Boolean annotationHandler(HttpServletRequest request, HttpServletResponse response, Object o,
                                      SessionInfo sessionInfo, String requestUrl) throws Exception {
        HandlerMethod handler = null;
        try {
            handler = (HandlerMethod) o;
        } catch (ClassCastException e) {
//                logger.error(e.getMessage(),e);
        }

        if (handler != null) {
            Object bean = handler.getBean();
            //需要登录
            RequiresUser methodRequiresUser = handler.getMethodAnnotation(RequiresUser.class);
            if (methodRequiresUser != null && methodRequiresUser.required() == false) {
                return true;
            }

            if (methodRequiresUser == null) {//类注解处理
                RequiresUser classRequiresUser = this.getAnnotation(bean.getClass(), RequiresUser.class);
                if (classRequiresUser != null && classRequiresUser.required() == false) {
                    return true;
                }
            }

        }

        return null;
    }


    /**
     * 数据库权限处理
     *
     * @param request
     * @param response
     * @param handler
     * @param sessionInfo
     * @param requestUrl
     * @return
     * @throws Exception
     */
    private Boolean dbHandler(HttpServletRequest request, HttpServletResponse response, Object handler,
                              SessionInfo sessionInfo, String requestUrl) throws Exception {
        // 不拦截的URL
        if (Collections3.isNotEmpty(excludeUrls)) {
            for (String excludeUrl : excludeUrls) {
                boolean flag = StringUtils.simpleWildcardMatch(excludeUrl, requestUrl);
                if (flag) {
                    return true;
                }
            }
        }

        if (sessionInfo != null) {
            //是否超时
            return true;
        } else {
            String url = SecurityConstants.SESSION_UNAUTHORITY_LOGIN_PAGE;
            if(isWx(handler)){
                url = SecurityConstants.WX_UNAUTHORITY_LOGIN_PAGE;
            }
            request.getRequestDispatcher(url).forward(request, response);
            return false; //返回到登录页面
        }
    }


    private <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotationType) {
        T result = clazz.getAnnotation(annotationType);
        if (result == null) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                return getAnnotation(superclass, annotationType);
            } else {
                return null;
            }
        } else {
            return result;
        }
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {


    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        if (e != null) {

        }
    }


    public List<String> getExcludeUrls() {
        return excludeUrls;
    }

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }


    public boolean isWx(Object o) {
        HandlerMethod handler = null;
        try {
            handler = (HandlerMethod) o;
        } catch (ClassCastException e) {
            // logger.error(e.getMessage(),e);
        }

        if (handler != null) {
            Object bean = handler.getBean();
            WxSite methodWxSite = handler.getMethodAnnotation(WxSite.class);
            if (methodWxSite != null && methodWxSite.isWx() == true) {
                return true;
            }

            if (methodWxSite == null) {//类注解处理
                WxSite classWxSite = this.getAnnotation(bean.getClass(), WxSite.class);
                if (classWxSite != null && classWxSite.isWx() == true) {
                    return true;
                }
            }
        }
        return false;
    }
}
