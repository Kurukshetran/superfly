package com.payneteasy.superfly.password;

import org.easymock.EasyMock;

import junit.framework.TestCase;

public class UserPasswordEncoderImplTest extends TestCase {
	private UserPasswordEncoderImpl userPasswordEncoder;
	private SaltSource saltSource;
	
	public void setUp() {
		userPasswordEncoder = new UserPasswordEncoderImpl();
		userPasswordEncoder.setPasswordEncoder(new PlaintextPasswordEncoder());
		saltSource = EasyMock.createMock(SaltSource.class);
		userPasswordEncoder.setSaltSource(saltSource);
	}
	
	public void testEncodeWithUsername() {
		EasyMock.expect(saltSource.getSalt("user")).andReturn("salt");
		
		EasyMock.replay(saltSource);
		
		String enc = userPasswordEncoder.encode("password", "user");
		assertEquals("password{salt}", enc);
		
		EasyMock.verify(saltSource);
	}
	
	public void testEncodeWithUserid() {
		EasyMock.expect(saltSource.getSalt(1L)).andReturn("salt");
		
		EasyMock.replay(saltSource);
		
		String enc = userPasswordEncoder.encode("password", 1L);
		assertEquals("password{salt}", enc);
		
		EasyMock.verify(saltSource);
	}
}
