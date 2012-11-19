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

package com.predic8.soamodel

import groovy.xml.*

import com.predic8.schema.*
import com.predic8.schema.diff.*
import com.predic8.xml.util.*

class DiffGeneratorTest extends GroovyTestCase {
  
  def comType1 = new ComplexType(qname : new QName('urn','a'))
  def comType2 = new ComplexType(qname : new QName('urn','b'))
  def comType3 = new ComplexType(qname : new QName('urn','a'))
  def comType4 = new ComplexType(qname : new QName('urn','c'))
  def comTypesA = [comType1, comType2]
  def comTypesB = [comType3, comType4]
  
  void testAbstractDiffGenerator(){
    def diffGen = new SchemaDiffGenerator()
    assertEquals(2, diffGen.compare(comTypesA, comTypesB, {"${it.qname} removed"}, {"${it.qname} added"}).size())
  }
  
  void testDoCompare(){
    def diffGen = new SchemaDiffGenerator()
    def msgs = diffGen.doCompare(comTypesA, comTypesB, {"${it.qname} removed"}, {"${it.qname} added"}, {it.qname})
    assertEquals(2, msgs.size())
    assertTrue(msgs*.toString().contains('{urn}b removed'))
    assertTrue(msgs*.toString().contains('{urn}c added'))
  }
}