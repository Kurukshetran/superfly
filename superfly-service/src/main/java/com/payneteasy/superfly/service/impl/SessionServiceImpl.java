package com.payneteasy.superfly.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.payneteasy.superfly.model.SSOSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import com.payneteasy.superfly.dao.SessionDao;
import com.payneteasy.superfly.model.RoutineResult;
import com.payneteasy.superfly.model.ui.session.UISession;
import com.payneteasy.superfly.notification.LogoutNotification;
import com.payneteasy.superfly.notification.Notifier;
import com.payneteasy.superfly.service.SessionService;

@Transactional
public class SessionServiceImpl implements SessionService {
	
	private static final Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);
	
	private SessionDao sessionDao;
	private Notifier notifier;

	@Required
	public void setSessionDao(SessionDao sessionDao) {
		this.sessionDao = sessionDao;
	}

	@Required
	public void setNotifier(Notifier notifier) {
		this.notifier = notifier;
	}

	public List<UISession> getExpiredSessions() {
		return sessionDao.getExpiredSessions();
	}

	public List<UISession> getInvalidSessions() {
		return sessionDao.getInvalidSessions();
	}

	public RoutineResult expireInvalidSessions() {
		return sessionDao.expireInvalidSessions();
	}
	
	public List<UISession> deleteExpiredSessionsAndNotify() {
		return deleteExpiredSessionsAndNotify(null);
	}

	public List<UISession> deleteExpiredSessionsAndNotify(Date beforeWhat) {
		List<UISession> sessions = sessionDao.deleteExpiredSessions(beforeWhat);
		if (logger.isDebugEnabled()) {
			logger.debug("Deleted " + sessions.size() + " sessions"
					+ (sessions.size() > 0 ? ", going to notify subsystems" : ""));
		}
		if (!sessions.isEmpty()) {
			Map<String, List<String>> callbackUriToSessionIds = new HashMap<String, List<String>>();
			for (UISession session : sessions) {
				if (session.getCallbackInformation() != null) {
					String uri = session.getCallbackInformation();
					List<String> sessionIds = callbackUriToSessionIds.get(uri);
					if (sessionIds == null) {
						sessionIds = new ArrayList<String>();
						callbackUriToSessionIds.put(uri, sessionIds);
					}
					sessionIds.add(String.valueOf(session.getId()));
				}
			}
			List<LogoutNotification> notifications = new ArrayList<LogoutNotification>(callbackUriToSessionIds.size());
			for (Entry<String, List<String>> entry : callbackUriToSessionIds.entrySet()) {
				LogoutNotification notification = new LogoutNotification();
				notification.setCallbackUri(entry.getKey());
				notification.setSessionIds(entry.getValue());
				notifications.add(notification);
			}
			notifier.notifyAboutLogout(notifications);
		}
		return sessions;
	}

	public List<UISession> deleteExpiredAndOldSessionsAndNotify(int seconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, -seconds);
		return deleteExpiredSessionsAndNotify(calendar.getTime());
	}

    @Override
    public SSOSession getValidSSOSession(String ssoSessionIdentifier) {
        return sessionDao.getValidSSOSession(ssoSessionIdentifier);
    }

}
