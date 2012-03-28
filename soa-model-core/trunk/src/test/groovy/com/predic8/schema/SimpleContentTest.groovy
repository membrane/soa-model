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

import groovy.xml.QName
import com.predic8.xml.util.*
import com.predic8.schema.diff.SchemaDiffGenerator 

class SimpleContentTest extends GroovyTestCase {
  
  def schema
  def element
  
  void setUp(){
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse(input:"/schema/simplecontent/attributeWithSimpleType.xsd")
    element = schema.getElement('shoeSize')
  }
  
  void testParsing() {
    assertEquals(new QName('http://www.w3.org/2001/XMLSchema','decimal'), element.embeddedType.model.extension.base)
    assertEquals('sizing', element.embeddedType.model.extension.attributes[0].name)
    assertEquals('string', element.embeddedType.model.extension.attributes[0].simpleType.restriction.base.localPart)
    assertEquals('optional', element.embeddedType.model.extension.attributes[0].use)
  }
  
  void testRequestTemplateCreator(){
    def requestTemplate = new XmlSlurper().parseText(element.requestTemplate)
    assertEquals('?999.99?', requestTemplate.text())
  }
  
  void testSchemaDiffGenerator(){
    def diffGen = new SchemaDiffGenerator()
    SimpleContent a = element.embeddedType.model
    def b = new SimpleContent(extension: new Extension())
    b.extension.base = new QName('http://www.w3.org/2001/XMLSchema','int')
    def diffs = diffGen.compareSimpleContent(a, b)
    assertEquals(1, diffs.size())
    assertTrue(diffs.toString().contains("SimpleContent has changed"))
    assertTrue(diffs.toString().contains("Extension base has changed from {http://www.w3.org/2001/XMLSchema}decimal to {http://www.w3.org/2001/XMLSchema}int."))
  }
}

