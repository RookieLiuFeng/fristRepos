package com.sharewin.core.security;

import com.google.common.collect.Lists;
import com.sharewin.common.model.Datagrid;
import com.sharewin.common.spring.SpringContextHolder;
import com.sharewin.common.utils.IPUtils;
import com.sharewin.common.utils.StringUtils;
import com.sharewin.common.web.springmvc.SpringMVCHolder;
import com.sharewin.modules.sys.entity.User;
import com.sharewin.modules.sys.service.UserManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * 系统使用的特殊工具类 简化代码编写.
 */
public class SecurityUtils {

	private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);
	private static UserManager userService = SpringContextHolder.getBean(UserManager.class);
	private static ApplicationSessionContext applicationSessionContext = ApplicationSessionContext.getInstance();

	/**
	 * User转SessionInfo.

	 * @param user
	 * @return
	 */
	public static SessionInfo userToSessionInfo(User user) {
		SessionInfo sessionInfo = new SessionInfo();
		sessionInfo.setUserId(user.getId());
		sessionInfo.setName(user.getRealname());
		sessionInfo.setLoginName(user.getLoginName());
		return sessionInfo;
	}

	/**
	 * 将用户放入session中.
	 *
	 * @param user
	 */
	public static synchronized void putUserToSession(HttpServletRequest request, User user) {
		HttpSession session = request.getSession();
		String sessionId = session.getId();
		if (logger.isDebugEnabled()) {
			logger.debug("putUserToSession:{}", sessionId);
		}
		SessionInfo sessionInfo = userToSessionInfo(user);
		sessionInfo.setIp(IPUtils.getIpAddr(request));
		sessionInfo.setId(sessionId);
		request.getSession().setAttribute(SecurityConstants.SESSION_SESSIONINFO, sessionInfo);
		SecurityConstants.sessionInfoMap.put(sessionId, sessionInfo);
		applicationSessionContext.addSession(session);
	}

	/**
	 * 获取当前用户session信息.
	 */
	public static SessionInfo getCurrentSessionInfo() {
		SessionInfo sessionInfo = null;
		try {
			sessionInfo = SpringMVCHolder.getSessionAttribute(SecurityConstants.SESSION_SESSIONINFO);
		} catch (Exception e) {
			// logger.error(e.getMessage(),e);
		}
		return sessionInfo;
	}

	/**
	 * 获取当前登录用户信息.
	 */
	public static User getCurrentUser() {
		SessionInfo sessionInfo = getCurrentSessionInfo();
		User user = null;
		if (sessionInfo != null) {
			user = userService.loadById(sessionInfo.getUserId());
		}
		return user;
	}

	/**
	 * 根据用户ID获取用户对象
	 * 
	 * @param userId
	 * @return
	 */
	public static User getUserById(String userId) {
		Integer uId = Integer.valueOf(userId);
		User user = null;
		if (uId != null) {
			user = userService.loadById(uId);
		}
		return user;
	}

	/**
	 * 将用户信息从session中移除
	 *
	 * @param sessionId session ID
	 * @param saveLog  是否保存切面日志
	 */
	public static synchronized void removeUserFromSession(String sessionId, boolean saveLog,
			SecurityType securityType) {
		if (StringUtils.isNotBlank(sessionId)) {
			Set<String> keySet = SecurityConstants.sessionInfoMap.keySet();
			for (String key : keySet) {
				if (key.equals(sessionId)) {
					if (logger.isDebugEnabled()) {
						logger.debug("removeUserFromSession:{}", sessionId);
					}
					if (saveLog) {
						SessionInfo sessionInfo = SecurityConstants.sessionInfoMap.get(key);
						//securityLogAspect.saveLog(sessionInfo, null, securityType);
					}
					SecurityConstants.sessionInfoMap.remove(key);
				}
			}
			HttpSession session = applicationSessionContext.getSession(sessionId);
			if (session != null) {
				session.removeAttribute(SecurityConstants.SESSION_SESSIONINFO);
				applicationSessionContext.removeSession(session);
			}
		}
	}

	/**
	 * 查看当前登录用户信息
	 *
	 * @return
	 */
	public static Datagrid<SessionInfo> getSessionUser() {
		List<SessionInfo> list = Lists.newArrayList();
		Set<String> keySet = SecurityConstants.sessionInfoMap.keySet();
		for (String key : keySet) {
			SessionInfo sessionInfo = SecurityConstants.sessionInfoMap.get(key);
			list.add(sessionInfo);
		}
		// 排序
		Collections.sort(list, new Comparator<SessionInfo>() {
			@Override
			public int compare(SessionInfo o1, SessionInfo o2) {
				return o2.getLoginTime().compareTo(o1.getLoginTime());
			}
		});

		Datagrid<SessionInfo> dg = new Datagrid<SessionInfo>(SecurityConstants.sessionInfoMap.size(), list);
		return dg;
	}

	/**
	 * 查看某个用户登录信息
	 * 
	 * @param loginName
	 *            登录帐号
	 * @return
	 */
	public static List<SessionInfo> getSessionUser(String loginName) {
		Datagrid<SessionInfo> datagrid = getSessionUser();
		List<SessionInfo> sessionInfos = Lists.newArrayList();
		for (SessionInfo sessionInfo : datagrid.getRows()) {
			if (sessionInfo.getLoginName().equals(loginName)) {
				sessionInfos.add(sessionInfo);
			}
		}
		return sessionInfos;
	}

	/**
	 * 根据SessionId查找对应的SessionInfo信息
	 * 
	 * @param sessionId
	 * @return
	 */
	public static SessionInfo getSessionInfo(String sessionId) {
		Datagrid<SessionInfo> datagrid = getSessionUser();
		for (SessionInfo sessionInfo : datagrid.getRows()) {
			if (sessionInfo.getId().equals(sessionId)) {
				return sessionInfo;
			}
		}
		return null;
	}

}
