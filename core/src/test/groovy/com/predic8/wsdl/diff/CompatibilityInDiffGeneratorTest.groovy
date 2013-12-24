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

class CompatibilityInDiffGeneratorTest extends GroovyTestCase {

	Definitions wsdl1
	Definitions wsdl2

	void setUp() {
		def parser = new WSDLParser(resourceResolver : new ClasspathResolver())
		wsdl1 = parser.parse('diff/compatibility/BLZService1.wsdl')
		wsdl2 = parser.parse('diff/compatibility/BLZService2.wsdl')
	}

	void testElementAdded2Req() {
		def diffs = compare(wsdl1, wsdl2)
		assert diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[1].diffs[0].diffs[0].description ==
		"Element ElementWithMinOcc1 with minOccurs 1 removed."
		assert !diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[1].diffs[0].diffs[0].safe
		assert !diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[1].diffs[0].diffs[0].warning
		assert diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[1].diffs[0].diffs[0].breaks
		assert diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[1].diffs[0].diffs[1].description ==
		"Element test with minOccurs 0 added to position 5(end of sequence)."
		assert !diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[1].diffs[0].diffs[1].safe
		assert diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[1].diffs[0].diffs[1].warning
		assert !diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[1].diffs[0].diffs[1].breaks
		
		assert diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[1].diffs[0].diffs[0].breaks
		
		assert diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].description == 
		"Element details:"
		
		assert diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].description == 
		"MinOccurs changed from 1 to 0."
		assert diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].warning
	}
	
	void testElementMin0Removed() {
		def diffs = compare(wsdl2, wsdl1)
		assert diffs[0].diffs[0].diffs[0].diffs[0].description == 'Output:'
		assert diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].description ==
		"MinOccurs changed from 0 to 1."
		assert !diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].breaks
		assert diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].safe
		//Warnin is false because the change is relative to response and not request.
		assert !diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].diffs[0].warning
	}
	
	private def compare(a, b) {
		new WsdlDiffGenerator(a: a, b: b).compare()
	}
}
