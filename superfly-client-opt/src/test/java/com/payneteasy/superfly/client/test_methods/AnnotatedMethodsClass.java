package com.payneteasy.superfly.client.test_methods;

import org.springframework.security.annotation.Secured;

public interface AnnotatedMethodsClass {
	@Secured("method1")
	void method1();
	
	@Secured("method2")
	void method2();
}
