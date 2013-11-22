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
		assert diffs[0].description == 'Element bar removed.'
	}
	
	void testRootElementsReplaced() {
		def diffs = compare(schemaWithRef, schemaWithName)
		assert diffs[0].description == 'Element foo removed.'
	}
	
	void testElemensInSequence() {
		def diffs = compare(schemaWithRef, schemaWithName)
		assert diffs[1].dump().contains('Element S1E1 removed.')
	}
	
	void testAll() {
		def diffs = compare(schemaWithRef, schemaWithName)
//		println diffs*.dump()
	}
	
	

//	void testCompareRef2Name() {
//		def diffs = compare(schemaWithRef, schemaWithName)
//		assert diffs.size() == 2
//		println diffs*.dump()
//		assert diffs[1].dump().contains('Element FirstElement with minoccurs 1 added.')
//		assert diffs[1].dump().contains('Element ref to FirstElement with minoccurs 0 removed.')
//	}

	private def compare(a, b) {
		new SchemaDiffGenerator(a: a, b: b).compare()
	}
}