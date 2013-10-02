package com.predic8.wsdl

import groovy.xml.MarkupBuilder

import com.predic8.wsdl.creator.WSDLCreator
import com.predic8.wsdl.creator.WSDLCreatorContext
import com.predic8.xml.util.ClasspathResolver

class WSDLImportWSDLTest extends GroovyTestCase {
	
	Definitions wsdl
	
	protected void setUp() throws Exception {
		WSDLParser parser = new WSDLParser(resourceResolver: new ClasspathResolver())
		wsdl = parser.parse('blz-with-import/BLZService.wsdl')
	}
	
	void testParser() {
		// There are three WSDL documents, but two have recursive imports, so registry has four WSDLs.
		assert 4 == wsdl.registry.getWsdls(wsdl.targetNamespace).size()
//		println wsdl.asString
	}

}
