package com.payneteasy.superfly.security.authentication;

import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;

import com.payneteasy.superfly.api.SSORole;
import com.payneteasy.superfly.api.SSOUser;
import com.payneteasy.superfly.security.RoleSource;
import com.payneteasy.superfly.security.StringTransformer;
import com.payneteasy.superfly.security.SuperflyAuthenticationProvider;

/**
 * Authentication implementation which represents authentication result
 * produced by SuperflyAuthenticationProvider on its last stage.
 * 
 * @author Roman Puchkovskiy
 * @see SuperflyAuthenticationProvider
 */
public class SSOUserAuthenticationToken implements Authentication {
	
	private SSOUser user;
	private SSORole role;
	private Object credentials;
	private Object details;
	private GrantedAuthority[] authorities;
	private boolean authenticated;
	
	public SSOUserAuthenticationToken(SSOUser user, SSORole role,
			Object credentials, Object details,
			StringTransformer[] transformers, RoleSource roleSource) {
		this.user = user;
		this.role = role;
		this.credentials = credentials;
		this.details = details;

		String[] roles = roleSource.getRoleNames(user, role);
		this.authorities = new GrantedAuthority[roles.length];
		for (int i = 0; i < roles.length; i++) {
			String name = roles[i];
			for (StringTransformer transformer : transformers) {
				name = transformer.transform(name);
			}
			this.authorities[i] = new GrantedAuthorityImpl(name);
		}
		
		this.authenticated = true;
	}
	
	public GrantedAuthority[] getAuthorities() {
		return authorities;
	}

	public Object getCredentials() {
		return credentials;
	}

	public Object getDetails() {
		return details;
	}

	public Object getPrincipal() {
		return user;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean isAuthenticated)
			throws IllegalArgumentException {
		this.authenticated = isAuthenticated;
	}

	public String getName() {
		return user.getName();
	}
	
	public SSOUser getUser() {
		return user;
	}
	
	public SSORole getRole() {
		return role;
	}

}
