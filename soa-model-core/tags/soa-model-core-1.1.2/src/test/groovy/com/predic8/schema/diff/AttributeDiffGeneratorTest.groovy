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

import javax.xml.stream.*
import groovy.xml.*

import com.predic8.xml.util.*
import com.predic8.schema.*

class AttributeDiffGeneratorTest extends GroovyTestCase{

  def schema
  def a
  def b
  def c
  def d
  def e
  def f
  def g

  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse(input:"/schema/attribute/attr-diff.xsd")
    a = schema.getType('contactA').attributes
    b = schema.getType('contactB').attributes
    c = schema.getType('contactC').attributes
    d = schema.getType('contactD').attributes
    e = schema.getType('contactE').attributes
    f = schema.getType('contactF').attributes
    g = schema.getType('contactG').attributes
  }

  void testAwithB(){
    def diffGen = new AttributesDiffGenerator(a: a, b: b, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertEquals(0, diffs.size())
  }

  void testAwithC(){
    def diffGen = new AttributesDiffGenerator(a: a, b: c, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertTrue(diffs.description.toString().contains('The type has changed'))
    assertTrue(diffs.description.toString().contains('string'))
    assertTrue(diffs.description.toString().contains('NMTOKEN'))
  }

  void testAwithD(){
    def diffGen = new AttributesDiffGenerator(a: a, b: d, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertTrue(diffs.description.toString().contains('The attribute use has changed from optional to required'))
    assertTrue(diffs.description.toString().contains('The attribute fixed changed from null to Germany'))
  }

  void testAwithE(){
    def diffGen = new AttributesDiffGenerator(a: a, b: e, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertTrue(diffs.description.toString().contains('Attribute icq added.'))
  }

  void testAwithF(){
    def diffGen = new AttributesDiffGenerator(a: a, b: f, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertTrue(diffs.description.toString().contains('Attribute fax removed.'))
  }

  void testAwithG(){
    def diffGen = new AttributesDiffGenerator(a: a, b: g, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    //assertTrue(diffs.description.toString().contains('Attribute fax removed.'))
  }
}