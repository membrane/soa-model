package com.predic8.soamodel

import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser
import com.predic8.wsdl.WSDLVersion2NotSupportedException;
import com.predic8.xml.util.ClasspathResolver

class ExceptionsTest extends GroovyTestCase {
	
	WSDLParser parser
	
	protected void setUp() throws Exception {
		parser = new WSDLParser(resourceResolver: new ClasspathResolver())
	}
	
	void testWrongGrammarException() {
		assert shouldFail(WrongGrammarException) {
			parser.parse('a.xsd')
		} == "Expected root element '{http://schemas.xmlsoap.org/wsdl/}definitions' for the WSDL document but was '{http://www.w3.org/2001/XMLSchema}schema'."
	}
	
	void testWrongGrammarExceptionLocation() {
		try {
			parser.parse('a.xsd')
		} catch (WrongGrammarException e) {
			assert 3 == e.location.lineNumber 
			assert 150 == e.location.columnNumber
			assert 191 == e.location.characterOffset
		}
	}
	
	void testNamespaceNotDeclaredForReferenceException(){
		assert shouldFail(NamespaceNotDeclaredForReferenceException) {
			parser.parse('NamespaceNotDeclared4Test.wsdl')
		} == "No namespace declared for prefix 'foo', used to reference 'foo:bar' in element 'plz'."
	}

	void testNamespaceNotDeclaredForReferenceExceptionInWSDL(){
		assert shouldFail(NamespaceNotDeclaredForReferenceException) {
			Definitions wsdl = parser.parse('BLZService-NamespaceNotDeclaredForReferenceException.wsdl')
			println wsdl.messages.parts.element
		} == "No namespace declared for prefix 'test', used to reference 'test:getBank' in part 'parameters'."
	}
	
	void testWSDLVersion2NotSupportedException() {
		assert shouldFail(WSDLVersion2NotSupportedException) {
			parser.parse('wsdl2sample.wsdl')
		} == "WSDL 2.0 is not supported yet."
	}
	
}
