package com.predic8.wsdl

import groovy.xml.MarkupBuilder
import groovy.xml.QName as GQName

import com.predic8.wsdl.Definitions
import com.predic8.wsdl.WSDLParser
import com.predic8.wsdl.WSDLParserContext
import com.predic8.wsdl.creator.WSDLCreator
import com.predic8.wsdl.creator.WSDLCreatorContext
import com.predic8.xml.util.ClasspathResolver

class ParserImportAbsoluteTest extends GroovyTestCase {
	
	Definitions wsdl
	
//	void setUp() {
//		WSDLParser parser = new WSDLParser(resourceResolver: new ClasspathResolver())
//		wsdl = parser.parse(new WSDLParserContext(input:"import-absolute-path/stockquoteservice.wsdl"))
//	}
	
	void test() {
//    def strWriter = new StringWriter()
//    def creator = new WSDLCreator(builder : new MarkupBuilder(strWriter))
//    creator.createDefinitions(wsdl, new WSDLCreatorContext())
	}
	
//	void testNamespaces() {
//		assertTrue(wsdl.imports.namespace.contains('http://example.com/stockquote/definitions'))
//	}
//
//	void testGetPortTypeByQName() {
//		assertNotNull(wsdl.getPortType(new GQName('http://example.com/stockquote/definitions', 'StockQuotePortType')))
//		assertEquals(1, wsdl.getImportedWSDL('http://example.com/stockquote/definitions').portTypes.size())
//	}
//
//	void testGetMessageByQName() {
//		assertNotNull(wsdl.getMessage(new GQName('http://example.com/stockquote/messages', 'GetLastTradePriceInput')))
//		assertEquals(2, wsdl.getImportedWSDL('http://example.com/stockquote/messages').messages.size())
//	}
//
//	void testBindingByQName() {
//		assertNotNull(wsdl.getBinding(new GQName('http://example.com/stockquote/service', 'StockQuoteSoapBinding')))
//	}
//
//	void testServiceByQName() {
//		assertNotNull(wsdl.getService(new GQName('http://example.com/stockquote/service', 'StockQuoteService')))
//	}
//
//	void testCreator() {
//		def strWriter = new StringWriter()
//		def creator = new WSDLCreator(builder : new MarkupBuilder(strWriter))
//		creator.createDefinitions(wsdl, [:])
//		def createdWSDL = new XmlSlurper().parseText(strWriter.toString())
//	}

}
