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

import groovy.xml.*
import com.predic8.wstool.creator.*
import com.predic8.xml.util.ClasspathResolver

class ElementTest extends GroovyTestCase{
  
  def schema
  def static final compTypeName = new QName("http://predic8.com" , "compType")
   
  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse(input:"/schema/ElementTest.xsd")
  }
  
  void testElement() {
    assertEquals(4, schema.elements.size())
  }
  
  void testElementType() {
    assertEquals('string' , schema.elements[0].type.localPart)
  }
  
  void testElementName() {
    assertEquals('Text' , schema.elements[0].name)
  }
  
  void testElementMinOccurs() {
    assertEquals('0' , schema.getType(compTypeName).sequence.particles[0].minOccurs)
  }
  
  void testElementMaxOccurs() {
    assertEquals('3' , schema.getType(compTypeName).sequence.particles[0].maxOccurs)
  }
  
  void testReferencedSimpleType() {
    def typeName = schema.getElement('Farbauswahl').type
    assertNotNull(schema.getType(typeName))
  }
  
  void testCreateXMLFarbauswahl() {
    def strWriter = new StringWriter()
    def creator = new RequestTemplateCreator(builder : new MarkupBuilder(strWriter))
    schema.getElement('Farbauswahl').create(creator, new RequestTemplateCreatorContext())
    def Farbauswahl = new XmlSlurper().parseText(strWriter.toString())
    assertEquals('Farbauswahl' , Farbauswahl.name())
    assertEquals('???' , Farbauswahl.text())
  }
  
  void testElementDocumentation() {
    assertEquals('Docu', schema.getElement('datum').annotation.documentations[0].content.toString())
  }
}
