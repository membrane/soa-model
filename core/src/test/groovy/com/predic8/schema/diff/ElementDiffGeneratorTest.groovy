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

class ElementDiffGeneratorTest  extends GroovyTestCase{

  def schema
  def a
  def b

  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse(input:"/schema/element/element-diff.xsd")
    a = schema.getElement('Contact1')
    b = schema.getElement('Contact2')
  }

  void testContact1With2(){
    def diffGen = new ElementDiffGenerator(a: a, b: b, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertEquals(1, diffs.size())
  }

  void testContact2With1(){
    def diffGen = new ElementDiffGenerator(a: b, b: a, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertEquals(1, diffs.size())
  }

	void testMinOccursDiff() {
		def diffGen = new ElementDiffGenerator(a: schema.getElement('person1'), b: schema.getElement('person2'), generator : new SchemaDiffGenerator())
		def diffs = diffGen.compare()
		assertTrue(diffs*.dump().toString().contains('The attribute minOccurs of element secondname has changed from 0 to 1.'))
	}
	
}
