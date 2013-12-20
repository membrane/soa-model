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
  def commons1
  def commons2
  
  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema1 = parser.parse("/diff/original.xsd")
    schema2 = parser.parse("/diff/schema2.xsd")
    schema3 = parser.parse("/diff/schema3.xsd")
    schema4 = parser.parse("/diff/schema4.xsd")
    schemaKomplex = parser.parse("/diff/PriceList.xsd")
    commons1 = parser.parse("/diff/common1.xsd")
    commons2 = parser.parse("/diff/common2.xsd")
  }

  void testCompareSchema1WithSchema2() {
    def diffs = compare(schema1, schema2)
    assertEquals(16, diffs.size())
    assert diffs[0].breaks() == null
  }
  
  void testCompareSchema1WithSchema3(){
    def diffs = compare(schema1, schema3)
    assertEquals(9, diffs.size())
    assertEquals(1, diffs.findAll{it.type == 'element'}.size())
    assertEquals(4, diffs.findAll{it.type == 'simpleType'}.size())
    assert diffs[0].breaks() == null
  }

  void testCompareSchema1WithSchema4(){
    def strWriter = new StringWriter()
    def creator = new SchemaCreator(builder : new MarkupBuilder(strWriter))
    schema1.create(creator, new SchemaCreatorContext())
  
    def diffs = compare(schema1, schema4)
    assertEquals(1, diffs.findAll{it.type == 'element'}.size())
    assert diffs[0].breaks() == null
    assert diffs[0].safe() == null
  }
  
  void testSchema2WithSchema3(){
    def diffs = compare(schema2, schema3)
    assert diffs[0].breaks() == null
    //assertEquals(1, diffs.findAll{it.type == 'import'}.size())
  }
  
  void testKomplex() {
    def diffs = compare(schemaKomplex,schemaKomplex)
    assertEquals(0,diffs.size())
  }

  void testCommons1WithCommons2() {
    // Verify that an element replaced by a choice does not cause the comparison to fail.
    def diffs = compare(commons1, commons2)
		assert !diffs[0].safe
		assert !diffs[0].warning
    assert !diffs[0].breaks
		//Breaks is only true when the comparison is for a WSDL and so the exchange is set.
    commons2.getElement('ForeignVATNumber').exchange = 'request'
    diffs = compare(commons1, commons2)
    assert !diffs[0].safe
    assert !diffs[0].warning
		//Breaks or Safe information are only available for WSDLDiffs (Version 1.4.2.3 and later)
    assert !diffs[0].breaks
  }

  void testCommons2WithCommons1() {
    // Verify that a choice replaced by an element does not cause the comparison to fail
    def diffs = compare(commons2, commons1)
    assert diffs[0].breaks() == null
  }
  
  private def compare(a, b) {
    new SchemaDiffGenerator(a: a, b: b).compare()
  }
}