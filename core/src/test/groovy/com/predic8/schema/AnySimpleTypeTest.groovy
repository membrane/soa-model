/* Copyright 2012 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.schema

import groovy.test.GroovyTestCase

import javax.xml.stream.*
import groovy.xml.*
import groovy.namespace.*

import com.predic8.xml.util.*
import com.predic8.schema.creator.*
import com.predic8.wstool.creator.*

class AnySimpleTypeTest extends GroovyTestCase{

  Schema schema

  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse("schema/XSD Schema/schemas.xsd")
  }
	
	void testParser() {
		assert schema.getType('string')
	}
	
	void testSchemaCreatorWithBasedRestriction() {
		def strWriter = new StringWriter()
    def creator = new SchemaCreator(builder : new MarkupBuilder(strWriter))
    ((schema.getType('string')).create(creator, new SchemaCreatorContext()))
		def xsdString = new XmlSlurper().parseText(strWriter.toString())
		assert xsdString.restriction.@base == 'xsd:anySimpleType'
	}
	
	void testSchemaCreatorWithBaselessRestriction() {
		def strWriter = new StringWriter()
		def creator = new SchemaCreator(builder : new MarkupBuilder(strWriter))
		((schema.getType('IDREFS')).create(creator, new SchemaCreatorContext()))
		def idrefs = new XmlSlurper().parseText(strWriter.toString())
		assert idrefs.restriction.simpleType.list.@itemType == 'xs:IDREF'
		assert idrefs.restriction.minLength.@value == 1
	}
}
