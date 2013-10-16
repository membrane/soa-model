package com.predic8.policy

import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser
import com.predic8.xml.util.ClasspathResolver

class ExtendedPolicyParserTest extends GroovyTestCase {

	Definitions wsdl
	
	void setUp() {
		def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
		wsdl = parser.parse('extern/TasaService.wsdl')
	}
	
	void testParser() {
		assert wsdl.policies['TasaServiceSoapBinding_WSAM_Addressing_Policy'].policyItems[0] instanceof Addressing
	}
}
