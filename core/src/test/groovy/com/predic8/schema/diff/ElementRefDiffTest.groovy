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

class ElementRefDiffTest extends GroovyTestCase {

	def schemaWithRef
	def schemaWithName

	void setUp() {
		def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
		schemaWithRef = parser.parse("/diff/schema/Ref2NameInElement/ref.xsd")
		schemaWithName = parser.parse("/diff/schema/Ref2NameInElement/name.xsd")
	}

	void testRootElementsAdded() {
		def diffs = compare(schemaWithName, schemaWithRef)
		assert diffs[0].description == 'Element bar with minOccurs 1 removed.'
	}
	
	void testRootElementsReplaced() {
		def diffs = compare(schemaWithRef, schemaWithName)
		assert diffs[0].description == 'Element foo with minOccurs 1 removed.'
	}
	
	void testSequnceDiff() {
		def diffs = compare(schemaWithRef, schemaWithName)
		assert diffs[7].description == 'ComplexType CT4:'
		assert diffs[7].diffs[0].type == 'sequence'
		assert diffs[7].diffs[0].diffs[0].description == 'Element E1 with minOccurs 1 removed.'
		assert diffs[7].diffs[0].diffs[1].description == 'Element E2 with minOccurs 1 removed.'
		assert diffs[7].diffs[0].diffs[2].description == 'Position of element E3 changed from 3 to 2.'
		assert diffs[7].diffs[0].diffs[3].description == 'Position of element E4 changed from 4 to 3.'
		assert diffs[7].diffs[0].diffs[4].description == 'Element ref to tns:foo with minOccurs 1 removed.'
		assert diffs[7].diffs[0].diffs[5].description == 'Particle choice on position 6 replaced with any.'
		assert diffs[7].diffs[0].diffs[6].description == 'Element E5 with minOccurs 1 added to position 1.'
		assert diffs[7].diffs[0].diffs[7].description == 'Element E6 with minOccurs 1 added to position 4.'
		assert diffs[7].diffs[0].diffs[8].description == 'Element ref to tns:bar with minOccurs 1 added to position 5.'
		assert diffs[7].diffs[0].diffs[9].description == 'any added to position 7(end of sequence).'
	}
	
	void testRefInChoice() {
		//Simulate WSDLDiffGenerator with compare4WSDL:true
		def diffs = new SchemaDiffGenerator(a: schemaWithRef, b: schemaWithName, compare4WSDL:true).compare()
		assert diffs[3].diffs[0].description == "The type of element 'RefTestInChoice' changed from xsd:string to xsd:int."
		assert diffs[6].diffs[0].diffs[3].diffs[0].description == 'Element ref to tns:RefTestInChoice:'
		assert diffs[6].diffs[0].diffs[3].diffs[0].diffs[0].description == 'Element RefTestInChoice:'
		assert diffs[6].diffs[0].diffs[3].diffs[0].diffs[0].diffs[0].description == "The type of element 'RefTestInChoice' changed from xsd:string to xsd:int."
	}
	
	private def compare(a, b) {
		new SchemaDiffGenerator(a: a, b: b, compare4WSDL:true).compare()
	}
}