package com.sharewin.core.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户登录验证注解
 */
@Target({ElementType.TYPE, ElementType.METHOD,ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface WxSite {

    /**
     * 用户是否是微信端 是：true 否：false 默认值：true
     * @return
     */
    boolean isWx() default false;

}