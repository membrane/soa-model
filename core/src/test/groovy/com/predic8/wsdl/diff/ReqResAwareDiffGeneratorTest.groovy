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

import com.predic8.wsdl.*
import com.predic8.xml.util.*

class ReqResAwareDiffGeneratorTest extends GroovyTestCase {

	Definitions oldWSDL
	Definitions newWSDL

	void setUp() {
		def parser = new WSDLParser(resourceResolver : new ClasspathResolver())
		oldWSDL = parser.parse('diff/req-res-aware/old.wsdl')
		newWSDL = parser.parse('diff/req-res-aware/new.wsdl')
	}

	void testSchemaDiffsInDefinitions() {
		def diffs = compare(oldWSDL, newWSDL)
		//Definitions -> Types -> Schema -> CType -> Seq  -> Element
		assert diffs[0].diffs[1].diffs[0].diffs[0].diffs[0].diffs[0].description == 'Element newElementRequest with minOccurs 0 added to position 3(end of sequence).'
		assert !diffs[0].diffs[1].diffs[0].diffs[0].diffs[0].diffs[0].safe()
		assert !diffs[0].diffs[1].diffs[0].diffs[0].diffs[0].diffs[0].breaks()
		assert diffs[0].diffs[1].diffs[0].diffs[1].diffs[0].diffs[1].description == 'Element newElementResponse with minOccurs 0 added to position 3(end of sequence).'
		assert !diffs[0].diffs[1].diffs[0].diffs[1].diffs[0].diffs[1].safe()
		assert !diffs[0].diffs[1].diffs[0].diffs[1].diffs[0].diffs[1].breaks()
	}
	
	private def compare(a, b) {
		new WsdlDiffGenerator(a: a, b: b).compare()
	}
}
