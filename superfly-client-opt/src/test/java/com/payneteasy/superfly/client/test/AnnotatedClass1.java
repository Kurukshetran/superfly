package com.payneteasy.superfly.client.test;

import org.springframework.security.annotation.Secured;

@Secured("single")
public class AnnotatedClass1 {
	@Secured("method")
	public void annotatedMethod() {
		
	}
}
