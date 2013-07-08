package com.predic8.soamodel

import com.predic8.wsdl.WSDLParser
import com.predic8.xml.util.ClasspathResolver

class ExceptionsTest extends GroovyTestCase {
	
	WSDLParser parser
	
	protected void setUp() throws Exception {
		parser = new WSDLParser(resourceResolver: new ClasspathResolver())
	}
	
	void testWrongGrammerException(){
		shouldFail(WrongGrammerException) {
			parser.parse('a.xsd')
		}
	}
	
	void testNamespaceNotDeclaredForReferenceException(){
		shouldFail(NamespaceNotDeclaredForReferenceException) {
			parser.parse('NamespaceNotDeclared4Test.wsdl')
		}
	}
	
}
