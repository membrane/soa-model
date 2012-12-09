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
    }

    void testEqual() {
        def diffGen = new SequenceDiffGenerator(a: seqA , b: seqB, generator : new SchemaDiffGenerator())
        def diffs = dumpDiffs(diffGen.compare(), "equal types")
        assertEquals(0, diffs.size())
    }

    void testElementRemoved() {
        def diffGen = new SequenceDiffGenerator(a: seqA , b: seqC, generator : new SchemaDiffGenerator())
        def diffs = dumpDiffs(diffGen.compare(), "removed any element")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs.diffs.description.toString().contains('removed'))

        // removing an 'any' breaks compatibility, old messages with content there will be rejected
        assertTrue(diffs[0].breaks())
        assertTrue(diffs[0].breaks() != diffs[0].safe())
    }

    void testTightenedMinOccurs() {
        def diffGen = new SequenceDiffGenerator(a: seqA , b: seqD, generator : new SchemaDiffGenerator())
        def diffs = dumpDiffs(diffGen.compare(),"tighter minOccurs (bigger)")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('minOccurs of any has changed from 0 to 1.'))

        // tighter minOccurs breaks compatibility, old messages may not have enough
        assertTrue(diffs[0].breaks())
        assertTrue(diffs[0].breaks() != diffs[0].safe())
    }

    void testLoosenedMinOccurs() {
        def diffGen = new SequenceDiffGenerator(a: seqD , b: seqA, generator : new SchemaDiffGenerator())
        def diffs = dumpDiffs(diffGen.compare(), "looser minOccurs (smaller)")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('minOccurs of any has changed from 1 to 0.'))

        // loosened minOccurs doesn't break compatibility on its own, old messages will have enough
        assertFalse(diffs[0].breaks())
        assertTrue(diffs[0].breaks() != diffs[0].safe())
    }

    void testTightenedMaxOccurs() {
        def diffGen = new SequenceDiffGenerator(a: seqA , b: seqE, generator : new SchemaDiffGenerator())
        def diffs = dumpDiffs(diffGen.compare(), "tightened maxOccurs (smaller)")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('maxOccurs of any has changed from unbounded to 5.'))

        // tightened maxOccurs breaks compatibility, old messages may have too many
        assertTrue(diffs[0].breaks())
        assertTrue(diffs[0].breaks() != diffs[0].safe())
    }

    void testLoosenedMaxOccurs() {
        def diffGen = new SequenceDiffGenerator(a: seqE , b: seqA, generator : new SchemaDiffGenerator())
        def diffs = dumpDiffs(diffGen.compare(), "looser maxOccurs (smaller)")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('maxOccurs of any has changed from 5 to unbounded.'))

        // loosened maxOccurs doesn't break compatibility, old messages will never have too many
        assertFalse(diffs[0].breaks())
        assertTrue(diffs[0].breaks() != diffs[0].safe())
    }

    void testNamespaceChanged() {
        def diffGen = new SequenceDiffGenerator(a: seqA , b: seqF, generator : new SchemaDiffGenerator())
        def diffs = dumpDiffs(diffGen.compare(), "namespace changed")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('namespace attribute changed'))

        // changed namespace breaks compatibility because old messages will not comply with the new namespace
        assertTrue(diffs[0].breaks())
        assertTrue(diffs[0].breaks() != diffs[0].safe())
    }

    void testNamespaceRemoved() {
        def diffGen = new SequenceDiffGenerator(a: seqA , b: seqG, generator : new SchemaDiffGenerator())
        def diffs = dumpDiffs(diffGen.compare(), "namespace removed")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('namespace attribute removed'))

        // removing the namespace doesn't break compatibility as it relaxes the requirement on the content
        assertFalse(diffs[0].breaks())
        assertTrue(diffs[0].breaks() != diffs[0].safe())
    }

    void testNamespaceAdded() {
        def diffGen = new SequenceDiffGenerator(a: seqG , b: seqA, generator : new SchemaDiffGenerator())
        def diffs = dumpDiffs(diffGen.compare(), "namespace added")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('namespace attribute added'))

        // adding the namespace breaks compatibility as old messages may have content outside that namespace
        assertTrue(diffs[0].breaks())
        assertTrue(diffs[0].breaks() != diffs[0].safe())
    }

    void testProcessContentsLoosened() {
        def diffGen = new SequenceDiffGenerator(a: seqA , b: seqH, generator : new SchemaDiffGenerator())
        def diffs = dumpDiffs(diffGen.compare(), "processContents loosened (less strict)")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('processContents became less strict'))

        // making processContents less strict doesn't break compatibility, messages that were accepted before will still be
        assertFalse(diffs[0].breaks())
        assertTrue(diffs[0].breaks() != diffs[0].safe())
    }

    void testProcessContentsTightened() {
        def diffGen = new SequenceDiffGenerator(a: seqH , b: seqA, generator : new SchemaDiffGenerator())
        def diffs = dumpDiffs(diffGen.compare(), "processContents tightened (more strict)")
        assertEquals(1, diffs.size())
        assertEquals(1, diffs[0].diffs.size())
        assertTrue(diffs[0].diffs[0].diffs.description.toString().contains('processContents became more strict'))

        // making processContents more strict breaks compatibility, messages that were accepted before may be rejected now
        assertTrue(diffs[0].breaks())
        assertTrue(diffs[0].breaks() != diffs[0].safe())
    }


    private static List<Difference> dumpDiffs(List<Difference> diffs, String msg = null) {
        if (msg) System.out.println("\"" + msg + "\"");

        for (diff in diffs) {
            dumpDiff(diff, "")
        }

        System.out.println();

        diffs
    }

    private static void dumpDiff(Difference diff, String level) {
        def safe = diff.safe();
        def breaks = diff.breaks();

        System.out.println(level + diff.getDescription() + " { safe:" + safe + " breaks:" + breaks + " }");
        for (Difference localDiff : diff.getDiffs()){
            dumpDiff(localDiff, level + "  ");
        }
    }


}