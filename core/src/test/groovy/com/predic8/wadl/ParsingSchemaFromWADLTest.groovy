package com.predic8.wadl

import groovy.xml.MarkupBuilder
import groovy.json.JsonBuilder
import groovy.json.JsonOutput

import com.predic8.wadl.creator.JsonCreator
import com.predic8.wadl.creator.JsonCreatorContext
import com.predic8.xml.util.ClasspathResolver

class ParsingSchemaFromWADLTest extends GroovyTestCase {

	Application wadl

	protected void setUp() throws Exception {
		WADLParser parser = new WADLParser(resourceResolver: new ClasspathResolver())
		wadl = parser.parse('wadl/application.xml')
	}
	
	void testJsonCreator() {
		def e = wadl.grammars.getElement('barList')
		def ctx = new JsonCreatorContext()
		new JsonCreator().createElement(e, ctx)
		def json = new JsonBuilder()
		def map = ['barList':ctx.jsonElements['barList']]
		json map
		assert '{"barList":{"bars":{"title":"?XXX?"}}}' == json.toString()
	}
}
