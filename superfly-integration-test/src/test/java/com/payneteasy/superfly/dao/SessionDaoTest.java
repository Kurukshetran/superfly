package com.payneteasy.superfly.dao;

import java.util.Date;
import java.util.List;

import com.payneteasy.superfly.model.RoutineResult;
import com.payneteasy.superfly.model.SSOSession;
import com.payneteasy.superfly.model.ui.session.UISession;
import junit.framework.Assert;
import org.springframework.beans.factory.annotation.Autowired;

public class SessionDaoTest extends AbstractDaoTest {
	private SessionDao sessionDao;

    @Autowired
	public void setSessionDao(SessionDao sessionDao) {
		this.sessionDao = sessionDao;
	}
	
	public void testGetInvalidSessions() {
		List<UISession> sessions = sessionDao.getInvalidSessions();
		assertNotNull("Must get some result", sessions);
	}
	
	public void testGetExpiredSessions() {
		List<UISession> sessions = sessionDao.getExpiredSessions();
		assertNotNull("Must get some result", sessions);
	}
	
	public void testExpireInvalidSessions() {
		RoutineResult result = sessionDao.expireInvalidSessions();
		assertRoutineResult(result);
	}
	
	public void testDeleteExpiredSessions() {
		List<UISession> sessions = sessionDao.deleteExpiredSessions(new Date());
		assertNotNull("Must get some result", sessions);
	}

    public void testDeleteExpiredSSOSession() {
        sessionDao.deleteExpiredSSOSessions(1);
    }

    public void testDeleteExpiredTokens() {
        sessionDao.deleteExpiredTokens(1);
    }
}
