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

package com.predic8.wsdl.diff

import com.predic8.schema.SchemaParser
import com.predic8.schema.diff.SchemaDiffGenerator
import com.predic8.wsdl.*
import com.predic8.xml.util.*

class CyclingElementRefDiffGeneratorTest extends GroovyTestCase {

	Definitions wsdl1
	Definitions wsdl2

	void setUp() {
		def parser = new WSDLParser(resourceResolver : new ClasspathResolver())
		wsdl1 = parser.parse('diff/cycling-refs/old.wsdl')
		wsdl2 = parser.parse('diff/cycling-refs/new.wsdl')
	}

	void testDocumentationInDefinitions() {
		def diffs = compare(wsdl1, wsdl2)
		assert diffs*.dump().toString().count('Element req') == 1
	}

	void testSchemaDiffAlternateRefTypeLoopConstruction () {
		def parser = new SchemaParser(resourceResolver : new ClasspathResolver())
		def schema1 = parser.parse('diff/endless-loop/RefTypeLoopMessages_v001.xsd')
		def schema2 = parser.parse('diff/endless-loop/RefTypeLoopMessages_v001.xsd')
		def diffs = new SchemaDiffGenerator(a:schema1, b:schema2, compare4WSDL: true).compare()
		//In case of failure would get to stackoverflow! 
		assert diffs == []
	}

	private def compare(a, b) {
		new WsdlDiffGenerator(a: a, b: b).compare()
	}
}
