package com.predic8.policy

import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser
import com.predic8.xml.util.ClasspathResolver

class PolicyInWSDLTest extends GroovyTestCase {

	Definitions wsdl
	
	void setUp() {
		def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
//		wsdl = parser.parse('policy/BLZService-with-policy.wsdl')
		wsdl = parser.parse('extern/usernameauthwithsymmkeydevelopmentdefault.wsdl')
	}
	
	void testParser() {
		assert wsdl.bindings[0].policyReference.uri == '#StockQuoteBindingPolicy'
		assert wsdl.policies.values().allPolicyItems.flatten().size() == 3
		assert wsdl.policies.size() == 3
	}
}
