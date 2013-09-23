package com.predic8.wadl.creator

import groovy.json.JsonBuilder

import com.predic8.schema.Schema
import com.predic8.schema.SchemaParser
import com.predic8.xml.util.ClasspathResolver

class JsonCreatorTest extends GroovyTestCase {

	Schema schema

	protected void setUp() throws Exception {
		SchemaParser parser = new SchemaParser(resourceResolver: new ClasspathResolver())
		schema = parser.parse('group.xsd')
	}
	
	void testJsonCreator() {
		def e = schema.getElement('employee')
		def ctx = new JsonCreatorContext()
		def jsonBuilder = new JsonBuilder()
		def creator = new JsonCreator(builder:jsonBuilder)
		creator.createElement(e, ctx)
		def map = ['employee':ctx.jsonElements['employee']]
		jsonBuilder map
		assert '{"employee":{"person":{"firstName":"?XXX?","lastName":"?XXX?"}}}' == jsonBuilder.toString()
	}
}
