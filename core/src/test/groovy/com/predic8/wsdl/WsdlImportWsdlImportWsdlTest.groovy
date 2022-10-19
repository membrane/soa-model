package com.predic8.wsdl

import groovy.test.GroovyTestCase
import groovy.xml.MarkupBuilder

import com.predic8.wsdl.creator.WSDLCreator
import com.predic8.wsdl.creator.WSDLCreatorContext
import com.predic8.xml.util.ClasspathResolver

class WsdlImportWsdlImportWsdlTest extends GroovyTestCase {
	
	Definitions wsdl
	
	protected void setUp() throws Exception {
		WSDLParser parser = new WSDLParser(resourceResolver: new ClasspathResolver())
		wsdl = parser.parse('article-service-mutiple-imports-includes/ArticleService.wsdl')
	}
	
	/**
	 * WSDL1 imports WSDL2 imports WSDL3 inlines XSD1 imports XSD2 imports XSD3 and XSD4 inclueds XSD4-1
	 */
	
	void testParser() {
		assert 3 == wsdl.registry.allWsdls.flatten().size()
		assert 4 == wsdl.registry.allWsdls.schemas.flatten().size()
	}
}
