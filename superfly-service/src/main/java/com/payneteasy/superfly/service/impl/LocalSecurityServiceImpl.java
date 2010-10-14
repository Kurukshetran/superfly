package com.payneteasy.superfly.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import com.payneteasy.superfly.dao.UserDao;
import com.payneteasy.superfly.hotp.NullHOTPProvider;
import com.payneteasy.superfly.model.AuthRole;
import com.payneteasy.superfly.password.PasswordEncoder;
import com.payneteasy.superfly.password.SaltSource;
import com.payneteasy.superfly.service.LocalSecurityService;
import com.payneteasy.superfly.service.LoggerSink;
import com.payneteasy.superfly.spi.HOTPProvider;

@Transactional
public class LocalSecurityServiceImpl implements LocalSecurityService {
	
	private static final Logger logger = LoggerFactory.getLogger(LocalSecurityServiceImpl.class);
	
	private UserDao userDao;
	private String localSubsystemName = "superfly";
	private String localRoleName = "admin";
	private LoggerSink loggerSink;
	private PasswordEncoder passwordEncoder;
	private SaltSource saltSource;
	private HOTPProvider hotpProvider = new NullHOTPProvider();

	@Required
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setLocalSubsystemName(String localSubsystemName) {
		this.localSubsystemName = localSubsystemName;
	}

	public void setLocalRoleName(String localRoleName) {
		this.localRoleName = localRoleName;
	}

	@Required
	public void setLoggerSink(LoggerSink loggerSink) {
		this.loggerSink = loggerSink;
	}

	@Required
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Required
	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}

	public void setHotpProvider(HOTPProvider hotpProvider) {
		this.hotpProvider = hotpProvider;
	}

	public String[] authenticate(String username, String password) {
		String encPassword = passwordEncoder.encode(password, saltSource.getSalt(username));
		List<AuthRole> roles = userDao.authenticate(username, encPassword,
				localSubsystemName, null, null);
		if (roles != null) {
			AuthRole role = null;
			for (AuthRole r : roles) {
				if (localRoleName.equals(r.getRoleName())) {
					role = r;
					break;
				}
			}
			if (role != null) {
				String[] result = new String[role.getActions().size()];
				for (int i = 0; i < result.length; i++) {
					result[i] = role.getActions().get(i).getActionName();
				}
				loggerSink.info(logger, "LOCAL_LOGIN", true, username);
				return result;
			}
		}
		loggerSink.info(logger, "LOCAL_LOGIN", false, username);
		return null;
	}

	public boolean authenticateUsingHOTP(String username, String hotp) {
		return hotpProvider.authenticate(username, hotp);
	}

}
