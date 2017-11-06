package com.sharewin.listener;

import com.sharewin.common.web.listenter.DefaultSystemInitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpSessionEvent;

public class SystemInitListener extends DefaultSystemInitListener {

	private static final Logger logger = LoggerFactory
			.getLogger(SystemInitListener.class);

	public SystemInitListener() {
	}

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        super.contextInitialized(sce);

    }

    /**
	 * session销毁
	 */
	public void sessionDestroyed(HttpSessionEvent evt) {
		super.sessionDestroyed(evt);
		String sessionId = evt.getSession().getId();
		//SecurityUtils.removeUserFromSession(sessionId,true, SecurityType.logout_abnormal);
	}

}
