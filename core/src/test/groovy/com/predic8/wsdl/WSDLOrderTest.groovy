package com.predic8.wsdl

import groovy.xml.MarkupBuilder

import com.predic8.wsdl.creator.WSDLCreator
import com.predic8.wsdl.creator.WSDLCreatorContext
import com.predic8.xml.util.ClasspathResolver

class WSDLOrderTest extends GroovyTestCase {

	Definitions wsdl1 
	Definitions wsdl2 
	WSDLParserContext ctx1 = new WSDLParserContext(input: 'BLZService.wsdl')
	WSDLParserContext ctx2 = new WSDLParserContext(input: 'upside-down/BLZUpsideDown.wsdl')
	
	protected void setUp() throws Exception {
		WSDLParser parser = new WSDLParser(resourceResolver: new ClasspathResolver())
		wsdl1 = parser.parse(ctx1)
		wsdl2 = parser.parse(ctx2)
	}

	void testUpsideDowParser() {
		assert ctx1.wsdlElementOrder.ELEMENTNAME[1 .. -1] == ctx2.wsdlElementOrder.ELEMENTNAME[-1 .. 1]
	}
	
}
