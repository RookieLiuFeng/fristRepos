package com.sharewin.core.security;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * session登录用户对象.
 */
@SuppressWarnings("serial")
public class SessionInfo implements Serializable {

    /**
     * sessionID
     */
    private String id;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 登录姓名
     */
    private String name;
    /**
     * 用户类型
     */
    private Integer userType;
    /**
     * 客户端IP
     */
    private String ip;
    /**
     * 角色ID集合
     */
    private List<Long> roleIds;
    /**
     * 角色名称组合
     */
    private String roleNames;

    /**
     * 登录时间
     */
    private Date loginTime = new Date();

    public SessionInfo() {
    }

    /**
     * sessionID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置 sessionID
     */
    public void setId(String id) {
        this.id = id;
    }

 
    /**
     * 用户ID
     */
    public Integer getUserId() {
		return userId;
	}

    /**
     * 设置 用户ID
     */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
     * 用户类型
     */
	public Integer getUserType() {
		return userType;
	}

	/**
     * 设置用户类型
     */
	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	/**
     * 登录名
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * 设置 登录名
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * 登录姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置 登录姓名
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * 客户端IP
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置 客户端IP
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 角色名称组合
     */
    public String getRoleNames() {
        return roleNames;
    }

    /**
     * 设置 角色名称组合
     */
    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    /**
     * 角色ID集合
     */
    public List<Long> getRoleIds() {
        return roleIds;
    }

    /**
     * 设置 角色ID集合
     */
    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    /**
     * 登录时间
     */
    // 设定JSON序列化时的日期格式
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getLoginTime() {
        return loginTime;
    }

    /**
     * 设置登录时间
     */
    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }


    /**
     * 是否是超级管理员
     * @return
     */
    public boolean isSuperUser(){
        if(this.getUserId() != null &&  this.getUserId().equals(1L)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
