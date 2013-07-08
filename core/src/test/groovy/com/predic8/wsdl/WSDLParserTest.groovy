package com.predic8.wsdl

import com.predic8.soamodel.WrongGrammerException;
import com.predic8.xml.util.ClasspathResolver

class WSDLParserTest extends GroovyTestCase {
	
	WSDLParser parser
	
	void testParseXSD(){
		parser = new WSDLParser(resourceResolver: new ClasspathResolver())
		shouldFail(WrongGrammerException) {
			parser.parse('a.xsd')
		}
	}
	
}
