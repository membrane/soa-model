package com.predic8.wsdl

import groovy.xml.MarkupBuilder

import com.predic8.wsdl.creator.WSDLCreator
import com.predic8.wsdl.creator.WSDLCreatorContext
import com.predic8.xml.util.ClasspathResolver

class UpsideDownWSDLTest extends GroovyTestCase {

	Definitions wsdl
	
	protected void setUp() throws Exception {
		WSDLParser parser = new WSDLParser(resourceResolver: new ClasspathResolver())
		wsdl = parser.parse('upside-down/BLZUpsideDown.wsdl')
	}

	void testUpsideDowParser() {
		assert wsdl
	}

	void testUpsideDownCreator() {
		assert wsdl.portTypes.operations.input.message.name == [['getBank']]
		assert wsdl.getAsString()
	}
	
}
