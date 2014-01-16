package com.predic8.wsdl

import com.predic8.xml.util.ExternalResolver
import groovy.xml.MarkupBuilder

import com.predic8.wsdl.creator.WSDLCreator
import com.predic8.wsdl.creator.WSDLCreatorContext
import com.predic8.xml.util.ClasspathResolver

class WSDLImportWSDLTest extends GroovyTestCase {
	
	Definitions wsdl
	
	void testParser() {
        WSDLParser parser = new WSDLParser(resourceResolver: new ClasspathResolver())
        wsdl = parser.parse('blz-with-import/BLZService.wsdl')
		// There are three WSDL documents, but two have recursive imports, so registry has four WSDLs.
		assert 4 == wsdl.registry.getWsdls(wsdl.targetNamespace).size()
	}
}
