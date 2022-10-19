package com.predic8.policy

import com.predic8.xml.util.ClasspathResolver
import groovy.test.GroovyTestCase

class PolicyParserTest extends GroovyTestCase {

	Policy policy
	
	void setUp() {
		def parser = new PolicyParser(resourceResolver: new ClasspathResolver())
		policy = parser.parse('policy/policy.xml')
	}
	
	void testParser() {
		assert 13 == policy.allPolicyItems.size()
	}
}
