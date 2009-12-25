package com.payneteasy.superfly.model.ui.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

/**
 * User object to be used in the UI (user list).
 * 
 * @author Roman Puchkovskiy
 */
public class UIUserForList implements Serializable {
	private long id;
	private String username;
	private String password;
	private boolean accountLocked;
	private int loginsFailed;
	private Date lastLoginDate;

	@Column(name = "user_id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "user_name")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "user_password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "is_account_locked")
	public boolean isAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	@Column(name = "logins_failed")
	public int getLoginsFailed() {
		return loginsFailed;
	}

	public void setLoginsFailed(int loginsFailed) {
		this.loginsFailed = loginsFailed;
	}

	@Column(name = "last_login_date")
	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
}
