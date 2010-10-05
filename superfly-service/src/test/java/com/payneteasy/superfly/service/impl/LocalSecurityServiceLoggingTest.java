package com.payneteasy.superfly.service.impl;


import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;

import java.util.Collections;

import org.easymock.EasyMock;
import org.slf4j.Logger;

import com.payneteasy.superfly.dao.UserDao;
import com.payneteasy.superfly.model.AuthRole;
import com.payneteasy.superfly.service.LocalSecurityService;

public class LocalSecurityServiceLoggingTest extends AbstractServiceLoggingTest {
	
	private LocalSecurityService localSecurityService;
	private UserDao userDao;
	
	public void setUp() {
		super.setUp();
		LocalSecurityServiceImpl service = new LocalSecurityServiceImpl();
		userDao = EasyMock.createStrictMock(UserDao.class);
		service.setUserDao(userDao);
		service.setLoggerSink(loggerSink);
		service.setLocalRoleName("local");
		localSecurityService = service;
	}
	
	public void testAuthenticateUser() throws Exception {
		AuthRole role = new AuthRole();
		role.setRoleName("local");
		EasyMock.expect(userDao.authenticate(eq("username"), eq("password"),
				anyObject(String.class), anyObject(String.class), anyObject(String.class)))
						.andReturn(Collections.singletonList(role));
		loggerSink.info(anyObject(Logger.class), eq("LOCAL_LOGIN"), eq(true), eq("username"));
		EasyMock.replay(loggerSink, userDao);
		
		localSecurityService.authenticate("username", "password");
		
		EasyMock.verify(loggerSink);
	}
	
	public void testAuthenticateUserFail() throws Exception {
		EasyMock.expect(userDao.authenticate(eq("username"), eq("password"),
				anyObject(String.class), anyObject(String.class), anyObject(String.class)))
						.andReturn(null);
		loggerSink.info(anyObject(Logger.class), eq("LOCAL_LOGIN"), eq(false), eq("username"));
		EasyMock.replay(loggerSink, userDao);
		
		localSecurityService.authenticate("username", "password");
		
		EasyMock.verify(loggerSink);
	}
	
}