package com.payneteasy.superfly.security.validator;

import java.util.ArrayList;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import junit.framework.TestCase;

import com.payneteasy.superfly.security.authentication.CompoundAuthentication;
import com.payneteasy.superfly.security.exception.PreconditionsException;

public class CompoundAuthenticationValidatorTest extends TestCase {
	private CompoundAuthenticationValidator validator;
	
	public void setUp() {
		validator = new CompoundAuthenticationValidator();
	}
	
	public void testValidateEmptyForEmpty() {
		validator.validate(new CompoundAuthentication());
	}
	
	public void testFail() {
		validator.setRequiredClasses(new Class<?>[]{UsernamePasswordAuthenticationToken.class});
		
		try {
			validator.validate(new CompoundAuthentication());
		} catch (PreconditionsException e) {
			// expected
		}
	}
	
	public void testNotEmptyForNotEmpty() {
		validator.setRequiredClasses(new Class<?>[]{UsernamePasswordAuthenticationToken.class});
		CompoundAuthentication compound = new CompoundAuthentication();
		compound.addReadyAuthentication(new UsernamePasswordAuthenticationToken("user", "password", new ArrayList<GrantedAuthority>()));
		validator.validate(compound);
	}
}
