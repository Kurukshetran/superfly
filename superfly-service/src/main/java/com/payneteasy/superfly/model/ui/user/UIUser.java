package com.payneteasy.superfly.model.ui.user;

import java.io.Serializable;

import javax.persistence.Column;

/**
 * User object to be used in the UI (create/update).
 * 
 * @author Roman Puchkovskiy
 */
public class UIUser implements Serializable {
	private Long id;
	private String username;
	private String password;
	private String email;
	
	@Column(name = "user_id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
	@Column(name = "user_email")
	public final String getEmail() {
		return email;
	}

	public final void setEmail(String email) {
		this.email = email;
	}
	
}
