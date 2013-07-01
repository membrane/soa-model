package com.predic8.policy

import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser
import com.predic8.xml.util.ClasspathResolver

class PolicyInWSDLTest extends GroovyTestCase {

	Definitions wsdl
	
	void setUp() {
		def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
		wsdl = parser.parse('policy/BLZService-with-policy.wsdl')
	}
	
	void testParser() {
		assert wsdl.policies
//		println "Policy Test in WSDL:"
//		println wsdl.policies
	}
}
