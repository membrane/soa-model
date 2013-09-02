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
		assert wsdl.bindings[0].policy
		wsdl.bindings[0].policy.allPolicyItems.each {
//			println it.ELEMENTNAME
		}
		wsdl.bindings[0].policy.allSecurityPolicies.each {
//			println it
		}
		
		assert wsdl.bindings.operations.input.flatten().policyReference.uri
		assert wsdl.bindings.operations.input.flatten().policy
	
		assert wsdl.bindings.operations.output.flatten().policyReference.uri
		assert wsdl.bindings.operations.output.flatten().policy

		assert wsdl.policies['StockQuoteBindingPolicy'].allPolicyItems.size() == 14
		assert wsdl.policies.size() == 3
	}
}
