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

import com.predic8.schema.SchemaParser
import com.predic8.xml.util.ClasspathResolver
import com.predic8.soamodel.Difference

class AnyDiffGeneratorTest extends GroovyTestCase{

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
    def seqL

    void setUp() {
        def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
        schema = parser.parse(input:"/schema/any/any-diff.xsd")
        seqA = schema.getType('NameTagA').model
        seqB = schema.getType('NameTagB').model
        seqC = schema.getType('NameTagC').model
        seqD = schema.getType('NameTagD').model
        seqE = schema.getType('NameTagE').model
        seqF = schema.getType('NameTagF').model
        seqG = schema.getType('NameTagG').model
        seqH = schema.getType('NameTagH').model
        seqI = schema.getType('NameTagI').model
        seqJ = schema.getType('NameTagJ').model
      seqK = schema.getType('NameTagK').model
      seqL = schema.getType('NameTagL').model
    }

    void testEqual() {
        def diffGen = new SequenceDiffGenerator(a: seqA , b: seqB, generator : new SchemaDiffGenerator())
				def diffs = diffGen.compare()
//        def diffs = dumpDiffs(diffGen.compare(), "equal types")
        assertEquals(0, diffs.size())
    }

    void testElementRemoved() {
        def diffGen = new SequenceDiffGenerator(a: seqA , b: seqC, generator : new SchemaDiffGenerator())
				def diffs = diffGen.compare()
//        def diffs = dumpDiffs(diffGen.compare(), "removed any element")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs.diffs.description.toString().contains('removed'))

        // removing an 'any' breaks compatibility, old messages with content there will be rejected
        assertTrue(diffs[0].breaks())
        assertTrue(diffs[0].breaks() != diffs[0].safe())
    }

    void testElementAddedRequired() {
        def diffGen = new SequenceDiffGenerator(a: seqC , b: seqD, generator : new SchemaDiffGenerator())
				def diffs = diffGen.compare()
//        def diffs = dumpDiffs(diffGen.compare(), "added any element with minOccurs=1")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs.diffs.description.toString().contains('Any added'))

        // adding an 'any' with minOccurs > 0 breaks compatibility, old messages without content there will be rejected
        assertTrue(diffs[0].breaks())
        assertTrue(diffs[0].breaks() != diffs[0].safe())
    }

    void testElementAddedNotRequired() {
        def diffGen = new SequenceDiffGenerator(a: seqC , b: seqA, generator : new SchemaDiffGenerator())
				def diffs = diffGen.compare()
//        def diffs = dumpDiffs(diffGen.compare(), "added any element with minOccurs=0")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs.diffs.description.toString().contains('Any added'))

        // adding an 'any' with minOccurs == 0 is safe, old messages without content there will be accepted
        assertTrue(diffs[0].safe() && !diffs[0].breaks() )
    }

    void testTightenedMinOccurs() {
        def diffGen = new SequenceDiffGenerator(a: seqA , b: seqD, generator : new SchemaDiffGenerator())
				def diffs = diffGen.compare()
//        def diffs = dumpDiffs(diffGen.compare(),"tighter minOccurs (bigger)")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('The attribute minOccurs of Element any  has changed from 0 to 1.'))

        // tighter minOccurs breaks compatibility, old messages may not have enough
        assertTrue(diffs[0].breaks() && !diffs[0].safe())
    }

    void testLoosenedMinOccurs() {
        def diffGen = new SequenceDiffGenerator(a: seqD , b: seqA, generator : new SchemaDiffGenerator())
				def diffs = diffGen.compare()
//        def diffs = dumpDiffs(diffGen.compare(), "looser minOccurs (smaller)")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('The attribute minOccurs of Element any  has changed from 1 to 0.'))

        // loosened minOccurs doesn't break compatibility on its own, old messages will have enough
        assertTrue(diffs[0].safe() && !diffs[0].breaks())
    }

    void testTightenedMaxOccurs() {
        def diffGen = new SequenceDiffGenerator(a: seqA , b: seqE, generator : new SchemaDiffGenerator())
				def diffs = diffGen.compare()
//        def diffs = dumpDiffs(diffGen.compare(), "tightened maxOccurs (smaller)")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('The attribute maxOccurs of Element any  has changed from unbounded to 5.'))

        // tightened maxOccurs breaks compatibility, old messages may have too many
        assertTrue(diffs[0].breaks() && !diffs[0].safe())
    }

    void testLoosenedMaxOccurs() {
        def diffGen = new SequenceDiffGenerator(a: seqE , b: seqA, generator : new SchemaDiffGenerator())
				def diffs = diffGen.compare()
//        def diffs = dumpDiffs(diffGen.compare(), "looser maxOccurs (smaller)")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('The attribute maxOccurs of Element any  has changed from 5 to unbounded.'))

        // loosened maxOccurs doesn't break compatibility, old messages will never have too many
				assertTrue(diffs[0].safe() && !diffs[0].breaks())
    }

    void testNamespaceChanged() {
        def diffGen = new SequenceDiffGenerator(a: seqA , b: seqF, generator : new SchemaDiffGenerator())
				def diffs = diffGen.compare()
//        def diffs = dumpDiffs(diffGen.compare(), "namespace changed")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('namespace attribute changed'))

        // changed namespace breaks compatibility because old messages will not comply with the new namespace
				assertTrue(diffs[0].breaks() && !diffs[0].safe())
    }

    void testNamespaceRemoved() {
        // this is equivalent to changing the namespace to any
        def diffGen = new SequenceDiffGenerator(a: seqA , b: seqG, generator : new SchemaDiffGenerator())
				def diffs = diffGen.compare()
//        def diffs = dumpDiffs(diffGen.compare(), "namespace removed")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('namespace attribute changed'))

        // removing the namespace doesn't break compatibility as the default of ##any relaxes the requirement on the content
        assertFalse(diffs[0].breaks())
        assertTrue(diffs[0].breaks() != diffs[0].safe())
    }

    void testNamespaceAdded() {
        def diffGen = new SequenceDiffGenerator(a: seqG , b: seqA, generator : new SchemaDiffGenerator())
				def diffs = diffGen.compare()
//        def diffs = dumpDiffs(diffGen.compare(), "namespace added")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('namespace attribute changed'))

        // adding a namespace (something other than ##any) breaks compatibility as old messages
        // may have content outside that namespace
        assertTrue(diffs[0].breaks())
        assertTrue(diffs[0].breaks() != diffs[0].safe())
    }

    // 1) if the new namespace is ##any, the change is safe
    // 2) if the the new namespace is ##other and the old namespace isn't ##any and
    //      doesn't contain ##targetNamespace, we're safe
    // 3) if b has a set of namespace options, they must be a superset of a's namespaces to be safe

    void testNamespaceChangedToOtherSafely() {
        def diffGen = new SequenceDiffGenerator(a: seqA , b: seqI, generator : new SchemaDiffGenerator())
				def diffs = diffGen.compare()
//        def diffs = dumpDiffs(diffGen.compare(), "namespace changed to ##other safely")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('namespace attribute changed'))

        // changing the namespace to ##other when the previous value was not ##targetNamespace or ##any
        // may have content outside that namespace
        assertFalse(diffs[0].breaks())
        assertTrue(diffs[0].breaks() != diffs[0].safe())
    }

    void testNamespaceChangedToOtherBreaks() {
        def diffGen = new SequenceDiffGenerator(a: seqK , b: seqI, generator : new SchemaDiffGenerator())
				def diffs = diffGen.compare()
//        def diffs = dumpDiffs(diffGen.compare(), "namespace changed to ##other breaks")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('namespace attribute changed'))

        // changing the namespace to ##other when the previous value was not ##targetNamespace or ##any
        // may have content outside that namespace
        assertTrue(diffs[0].breaks())
        assertTrue(diffs[0].breaks() != diffs[0].safe())
    }

    void testNamespaceChangedToSuperset() {
        def diffGen = new SequenceDiffGenerator(a: seqA , b: seqK, generator : new SchemaDiffGenerator())
				def diffs = diffGen.compare()
//        def diffs = dumpDiffs(diffGen.compare(), "namespace changed to superset")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('namespace attribute changed'))

        // changing the namespace to a superset of what it was is safe
        assertFalse(diffs[0].breaks())
        assertTrue(diffs[0].breaks() != diffs[0].safe())
    }

    void testNamespaceChangedToSubset() {
        def diffGen = new SequenceDiffGenerator(a: seqK , b: seqA, generator : new SchemaDiffGenerator())
				def diffs = diffGen.compare()
//        def diffs = dumpDiffs(diffGen.compare(), "namespace changed to subset")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('namespace attribute changed'))

        // changing the namespace to a subset of what it was breaks
        assertTrue(diffs[0].breaks())
        assertTrue(diffs[0].breaks() != diffs[0].safe())
    }

    void testProcessContentsLoosened() {
        def diffGen = new SequenceDiffGenerator(a: seqA , b: seqH, generator : new SchemaDiffGenerator())
				def diffs = diffGen.compare()
//        def diffs = dumpDiffs(diffGen.compare(), "processContents loosened (less strict)")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('processContents became less strict'))

        // making processContents less strict doesn't break compatibility, messages that were accepted before will still be
        assertFalse(diffs[0].breaks())
        assertTrue(diffs[0].breaks() != diffs[0].safe())
    }

    void testProcessContentsTightened() {
        def diffGen = new SequenceDiffGenerator(a: seqH , b: seqA, generator : new SchemaDiffGenerator())
				def diffs = diffGen.compare()
//        def diffs = dumpDiffs(diffGen.compare(), "processContents tightened (more strict)")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('processContents became more strict'))

        // making processContents more strict breaks compatibility, messages that were accepted before may be rejected now
        assertTrue(diffs[0].breaks())
        assertTrue(diffs[0].breaks() != diffs[0].safe())
    }

  void testProcessAddedElementBeforeAny() {
    def diffGen = new SequenceDiffGenerator(a: seqA , b: seqL, generator : new SchemaDiffGenerator())
    def diffs = diffGen.compare()
//    def diffs = dumpDiffs(diffGen.compare(), "element added before any")
    assertEquals(1, diffs.size())
    assertEquals(2, diffs[0].diffs.size())
    assertTrue(diffs[0].diffs[0].description.toString().contains('Position of element any changed'))

    // inserting an element with minOccurs=1 before the any is a breakage.
    // In the future, this could be smarter: elements with minOccurs=0
    // that are added before an "any" could be considered non-breaking
    assertTrue(diffs[0].breaks())
    assertTrue(diffs[0].breaks() != diffs[0].safe())
  }

}