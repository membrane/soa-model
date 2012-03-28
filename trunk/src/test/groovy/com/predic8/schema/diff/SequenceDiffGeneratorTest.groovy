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

class SequenceDiffGeneratorTest extends GroovyTestCase{

  def schema
  def seqA
  def seqB
  def seqC
  def seqD
  def seqE
  def seqF
  def seqG
  def seqH
  def seqI
  def seqJ
  def seqK

  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse(input:"/schema/sequence/sequence-diff.xsd")
    seqA = schema.getType('AddressA').model
    seqB = schema.getType('AddressB').model
    seqC = schema.getType('AddressC').model
    seqD = schema.getType('AddressD').model
    seqE = schema.getType('AddressE').model
    seqF = schema.getType('AddressF').model
    seqG = schema.getType('AddressG').model
    seqH = schema.getType('AddressH').model
    seqI = schema.getType('AddressI').model
    seqJ = schema.getType('AddressJ').model
    seqK = schema.getType('AddressK').model

  }

  void testEqual(){
    def diffGen = new SequenceDiffGenerator(a: seqA , b: seqB, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertEquals(0, diffs.size())
  }

  void testElementChanged(){
    def diffGen = new SequenceDiffGenerator(a: seqA , b: seqC, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertEquals(1, diffs.size())
    assertEquals(1, diffs[0].diffs.size())
    assertTrue(diffs.diffs.description.toString().contains('changed'))
  }

  void testElementRemoved(){
    def diffGen = new SequenceDiffGenerator(a: seqA , b: seqD, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertEquals(1, diffs.size())
    assertEquals(1, diffs[0].diffs.size())
    assertTrue(diffs.diffs.description.toString().contains('removed'))
  }

  void testElementReorderd(){
    def diffGen = new SequenceDiffGenerator(a: seqA , b: seqE, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertEquals(1, diffs.size())
    assertEquals(2, diffs[0].diffs.size())
    assertTrue(diffs.diffs.description.toString().contains('changed'))
  }

  void testElementAdded(){
    def diffGen = new SequenceDiffGenerator(a: seqA , b: seqF, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertEquals(1, diffs.size())
    assertEquals(4, diffs[0].diffs.size())
    assertTrue(diffs.diffs.description.toString().contains('added'))
  }

  void testElement2Sequence(){
    def diffGen = new SequenceDiffGenerator(a: seqF , b: seqG, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertEquals(1, diffs.size())
    assertTrue(diffs.diffs.description.toString().contains('removed'))
    assertTrue(diffs.diffs.description.toString().contains('added'))
  }

  void testElementsAndSequenceEquals(){
    def diffGen = new SequenceDiffGenerator(a: seqG , b: seqH, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertEquals(0, diffs.size())
  }

  void testSequence2Choice(){
    def diffGen = new SequenceDiffGenerator(a: seqG , b: seqI, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertEquals(1, diffs.size())
    assertEquals(1, diffs[0].diffs.size())
    assertTrue(diffs.diffs.description.toString().contains('replaced'))
  }

  void testChoice2Sequence(){
    def diffGen = new SequenceDiffGenerator(a: seqI , b: seqG, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertEquals(1, diffs.size())
    assertEquals(1, diffs[0].diffs.size())
    assertTrue(diffs.diffs.description.toString().contains('replaced'))
  }

  void testElementAddedInSequence(){
    def diffGen = new SequenceDiffGenerator(a: seqG , b: seqJ, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertEquals(1, diffs.size())
    assertEquals(2, diffs[0].diffs[0].diffs.size())
    assertTrue(diffs.diffs.diffs.description.toString().contains('added'))
  }

  void testAdditionalSequence(){
    def diffGen = new SequenceDiffGenerator(a: seqA , b: seqK, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
    assertEquals(1, diffs.size())
    assertTrue(diffs.diffs.description.toString().contains('sequence added'))
  }
}