package com.predic8.wsdl

import groovy.xml.MarkupBuilder

import com.predic8.wsdl.creator.WSDLCreator
import com.predic8.wsdl.creator.WSDLCreatorContext
import com.predic8.xml.util.ClasspathResolver
import com.predic8.schema.creator.*

class WSDLInlineSchemaTest extends GroovyTestCase {

	Definitions wsdl
	WSDLParserContext ctx = new WSDLParserContext(input: 'wsdl/inline-schema-in-wsdl/test.wsdl')
	
	protected void setUp() throws Exception {
		WSDLParser parser = new WSDLParser(resourceResolver: new ClasspathResolver())
		wsdl = parser.parse(ctx)
	}

	void testThatSchemaInsideWSDLCanBeRead() {
		def strWriter = new StringWriter()
		def creator = new SchemaCreator(builder : new MarkupBuilder(strWriter))
		wsdl.schemas[0].create(creator, new SchemaCreatorContext())
		def schemaAsString = strWriter.toString()
		def testSchema = new XmlSlurper().parseText(schemaAsString)
		assertEquals('xsd:anyType', testSchema.complexType[0].complexContent.restriction.@base.toString())
	}
	
}
