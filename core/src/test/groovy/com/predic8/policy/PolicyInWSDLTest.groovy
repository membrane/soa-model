package com.predic8.policy

import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser
import com.predic8.xml.util.ClasspathResolver

class PolicyInWSDLTest extends GroovyTestCase {

	Definitions wsdl

	void setUp() {
		def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
		wsdl = parser.parse('policy/PolicyArticle.wsdl')
	}

	void testParser() {
		assert wsdl.bindings[0].policyReference.uri == '#ArticleBindingPolicy'
		assert wsdl.bindings[0].policy

		assert wsdl.bindings.operations.input.flatten()[0].policyReference.uri == '#ArticleBinding_GetArticle_Input_Policy'
		assert wsdl.bindings.operations.input.flatten().policy

		assert wsdl.bindings.operations.output.flatten()[0].policyReference.uri == '#ArticleBinding_GetArticle_Output_Policy'
		assert wsdl.bindings.operations.output.flatten().policy

		assert wsdl.policies['ArticleBindingPolicy'].allPolicyItems.size() == 29
		assert wsdl.policies.size() == 3
	}
	
	void testPolicyAssertions() {
		wsdl.policies.values().each {
			assert it.asString
		}
		assert 'UsingAddressing' in wsdl.bindings[0].policyAssertions
		assert 'SymmetricBinding' in wsdl.bindings[0].policyAssertions
		assert 'UsernameToken' in wsdl.bindings[0].policyAssertions
		assert 'SignedParts' in wsdl.bindings[0].operations[0].input.policyAssertions
		assert 'EncryptedParts' in wsdl.bindings[0].operations[0].input.policyAssertions
		assert 'SignedParts' in wsdl.bindings[0].operations[0].output.policyAssertions
		assert 'EncryptedParts' in wsdl.bindings[0].operations[0].output.policyAssertions
	}
}
