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

import com.predic8.schema.creator.*
import com.predic8.wstool.creator.*
import com.predic8.xml.util.*

import groovy.xml.*
import groovy.namespace.*
class AnyTest extends GroovyTestCase{
  
  def schema

  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse("any.xsd")
  }
  
  void testParseAny() {
    assertTrue(schema.getElement('person').embeddedType.model.particles.elementName.contains('any'))
  }
  
  void testSchemaCreator(){
    def strWriter = new StringWriter()
    def creator = new SchemaCreator(builder : new MarkupBuilder(strWriter))
    schema.create(creator, new SchemaCreatorContext())
    def testSchema = new XmlSlurper().parseText(strWriter.toString())
    assertEquals("com.predic8.any", testSchema.element[0].complexType.sequence.any.@namespace.toString())
    assertEquals("0", testSchema.element[0].complexType.sequence.any.@minOccurs.toString())
  }
  
  void testRequestTemplateCreator() {
    def strWriter = new StringWriter()
    def creator = new RequestTemplateCreator(builder : new MarkupBuilder(strWriter))
    schema.getElement('person').create(creator, new RequestTemplateCreatorContext())
    assertTrue(strWriter.toString().contains('<!-- This element can be extended by any element from com.predic8.any namespace -->'))
    assertTrue(strWriter.toString().contains('<!-- This element can be extended by any attribute from com.predic8.anyAttribute namespace -->'))
  }
  
}
