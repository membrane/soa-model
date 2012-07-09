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

package com.predic8.schema.diff

import com.predic8.schema.*
import com.predic8.xml.util.*
import javax.xml.stream.*
import com.predic8.schema.creator.*
import groovy.xml.MarkupBuilder

class SchemaDiffGeneratorTest extends GroovyTestCase {
  
  def schema1
  def schema2
  def schema3
  def schema4
  def schemaKomplex
  
  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema1 = parser.parse(input:"/diff/original.xsd")
    schema2 = parser.parse(input:"/diff/schema2.xsd")
    schema3 = parser.parse(input:"/diff/schema3.xsd")
    schema4 = parser.parse(input:"/diff/schema4.xsd")
    schemaKomplex = parser.parse(input:"/diff/PriceList.xsd")
  }

  void testCompareSchema1WithSchema2() {
    def diffs = compare(schema1, schema2)
    assertEquals(1, diffs.size())
    assertTrue(diffs[0].breaks())
  }
  
  void testCompareSchema1WithSchema3(){
    def diffs = compare(schema1, schema3)
    assertEquals(9, diffs.size())
    assertEquals(1, diffs.findAll{it.type == 'element'}.size())
    assertEquals(4, diffs.findAll{it.type == 'simpleType'}.size())
    assertTrue(diffs[0].breaks())
  }

  void testCompareSchema1WithSchema4(){
    def strWriter = new StringWriter()
    def creator = new SchemaCreator(builder : new MarkupBuilder(strWriter))
    schema1.create(creator, new SchemaCreatorContext())
  
    def diffs = compare(schema1, schema4)
    assertEquals(1, diffs.findAll{it.type == 'element'}.size())
    assertTrue(diffs[0].breaks())
    assertFalse(diffs[0].safe())
//    println "***********" + diffs.findAll{it.type == 'complexType' && it.a?.qname?.localPart == 'EmployeeType'}
//    assertEquals(["Sequence has changed:"], diffs.findAll{it.type == 'complexType' && it.a?.qname?.localPart == 'EmployeeType'}.diffs.diffs.description.flatten())
//    TODO
//    assertEquals(["element shoesize added.", "element age added."] ,diffs.findAll{it.type == 'complexType' && it.a?.qname?.localPart == 'EmployeeType'}.diffs.diffs.diffs.description.flatten())
  }
  
  void testSchema2WithSchema3(){
    def diffs = compare(schema2, schema3)
    assertTrue(diffs[0].breaks())
    //assertEquals(1, diffs.findAll{it.type == 'import'}.size())
  }
  
  void testKomplex() {
    def diffs = compare(schemaKomplex,schemaKomplex)
    assertEquals(0,diffs.size())
  }
  
  private def compare(a, b) {
    new SchemaDiffGenerator(a: a, b: b).compare()
  }
}