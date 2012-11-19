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

class AllDiffGeneratorTest extends GroovyTestCase{

  def schema
  def a
  def b
  def c
  def d
  def e

  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse(input:"/schema/choice/choice-diff.xsd")
    a = schema.getType('contactA').model
    b = schema.getType('contactB').model
    c = schema.getType('contactC').model
    d = schema.getType('contactD').model
    e = schema.getType('contactE').model

  }

  void testEqual(){
    def diffGen = new ChoiceDiffGenerator(a: a, b: b, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertEquals(0, diffs.size())
  }

  void testElementremoved(){
    def diffGen = new ChoiceDiffGenerator(a: a , b: c, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertEquals(1, diffs.size())
    assertEquals(1, diffs[0].diffs.size())
    assertTrue(diffs.diffs.description.toString().contains('removed'))
  }

  void testElementAdded(){
    def diffGen = new ChoiceDiffGenerator(a: a , b: d, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertEquals(1, diffs.size())
    assertEquals(1, diffs[0].diffs.size())
    assertTrue(diffs.diffs.description.toString().contains('added'))
  }

  void testSequenceAdded(){
    def diffGen = new ChoiceDiffGenerator(a: a , b: e, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertEquals(1, diffs.size())
    assertEquals(1, diffs[0].diffs.size())
    assertTrue(diffs.diffs.description.toString().contains('added'))
  }

  void testSequenceRemoved(){
    def diffGen = new ChoiceDiffGenerator(a: e , b: a, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertEquals(1, diffs.size())
    assertEquals(1, diffs[0].diffs.size())
    assertTrue(diffs.diffs.description.toString().contains('removed'))
  }

}