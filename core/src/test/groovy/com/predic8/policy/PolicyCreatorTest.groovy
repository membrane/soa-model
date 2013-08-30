package com.predic8.policy

import groovy.xml.*

import com.predic8.wsdl.*
import com.predic8.wsdl.creator.*
import com.predic8.xml.util.ClasspathResolver

class PolicyCreatorTest extends GroovyTestCase {
	
	Definitions wsdl
	
	void setUp() {
		def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
//		wsdl = parser.parse('policy/BLZService-with-policy.wsdl')
		wsdl = parser.parse('extern/usernameauthwithsymmkeydevelopmentdefault.wsdl')
	}
	
	void testPolicyCreator() {
		def strWriter = new StringWriter()
		def creator = new WSDLCreator(builder : new MarkupBuilder(strWriter))
		creator.createDefinitions(wsdl, new WSDLCreatorContext())
		println strWriter
	}

}
