package org.payneteasy.superfly.client;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.payneteasy.superfly.client.exception.CollectionException;

import com.payneteasy.superfly.api.ActionDescription;

import junit.framework.TestCase;

public class ScanningActionDescriptionCollectorTest extends TestCase {
	public void testCollect() throws CollectionException {
		ScanningActionDescriptionCollector collector = new ScanningActionDescriptionCollector();
		collector.setBasePackages(new String[]{getClass().getPackage().getName() + ".test"});
		List<ActionDescription> descriptions = collector.collect();
		assertNotNull("Null result", descriptions);
		assertEquals("Wrong number of actions collected", 5, descriptions.size());

		Set<String> names = new HashSet<String>(descriptions.size());
		for (ActionDescription d : descriptions) {
			assertFalse("Duplicate name " + d.getName(), names.contains(d.getName()));
			names.add(d.getName());
		}
		
		assertTrue(names.contains("sub"));
		assertTrue(names.contains("single"));
		assertTrue(names.contains("multiple1"));
		assertTrue(names.contains("multiple2"));
		assertTrue(names.contains("nested"));
	}
}
