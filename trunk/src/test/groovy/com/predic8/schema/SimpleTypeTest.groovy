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

import junit.framework.TestCase
import javax.xml.stream.*
import groovy.xml.*

import com.predic8.wstool.creator.*
import com.predic8.xml.util.*
import com.predic8.schema.restriction.*
import com.predic8.schema.diff.*

class SimpleTypeTest extends GroovyTestCase{
  
  def token
  def schemaA
  def schemaB

  def static qname = new QName("http://thomas-bayer.com/blz/",'getBankType')
  
  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schemaA = parser.parse(input:"/schema/simpletype/language-a.xsd")
    schemaB = parser.parse(input:"/schema/simpletype/language-b.xsd")
  }
  
  void testSimpleType() {
    assertEquals(1, schemaA.simpleTypes.size())
  }
  
  void testRestriction() {
    assertTrue(schemaA.simpleTypes[0].restriction instanceof StringRestriction)
  }
  
  void testFacet() {
    assertEquals(2 , schemaA.simpleTypes[0].restriction.facets.size())
  }
  
  void testEnumerationNumbers() {
    assertEquals(5 , schemaA.simpleTypes[0].restriction.facets[0].values.size())
  }
  
  void testLengthFacet() {
    assertEquals('16' , schemaA.simpleTypes[0].restriction.lengthFacet.value)
  }
  
  void testGetSimpleTypeFromSchema() {
    def qname = new QName("http://predic8.com", "Language")
    assertNotNull(schemaA.getType(qname))
  }
  
  void testCreateXML() {
    def strWriter = new StringWriter()
    def creator = new RequestTemplateCreator(builder: new MarkupBuilder(strWriter))
    schemaA.elements[0].create(creator, new RequestTemplateCreatorContext())
    def Translate = new XmlSlurper().parseText(strWriter.toString())
    assertEquals('LanguageMode' , Translate.LanguageMode.name())
    assertEquals('???' , Translate.LanguageMode.text())
  }
  
  void testDocumentation() {
    assertTrue('Docu' in schemaA.getType('Language').annotation.documentations.content)
  }

  void testDiffGenerator(){
    def diffGen = new SchemaDiffGenerator(a: schemaA, b: schemaB)
    def diffs = diffGen.compare()
    assertEquals(1, diffs.size())
  }
}
