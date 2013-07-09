package com.predic8.wsdl

import groovy.xml.MarkupBuilder

import com.predic8.wsdl.creator.WSDLCreator
import com.predic8.wsdl.creator.WSDLCreatorContext
import com.predic8.xml.util.ClasspathResolver

class PartialWSDLTest extends GroovyTestCase {

	WSDLParser parser = new WSDLParser(resourceResolver: new ClasspathResolver())

	void testParseServices() {
		Definitions wsdl = parser.parse('partial-wsdl/BLZServices.wsdl')
		assert wsdl.services.size() == 1
	}

	void testParseTypes() {
		Definitions wsdl = parser.parse('partial-wsdl/BLZTypes.wsdl')
		assert wsdl.types.size() == 1
		assert wsdl.schemas.size() == 1
	}

	void testParseBindings() {
		Definitions wsdl = parser.parse('partial-wsdl/BLZBindings.wsdl')
		assert wsdl.bindings.size() == 3
	}

	void testParsePortTypes() {
		Definitions wsdl = parser.parse('partial-wsdl/BLZPortTypes.wsdl')
		assert wsdl.portTypes.size() == 1
	}

	void testParseMessages() {
		Definitions wsdl = parser.parse('partial-wsdl/BLZMessages.wsdl')
		assert wsdl.messages.size() == 2
	}
}
