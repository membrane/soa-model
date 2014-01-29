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

class OnlyOnePortTypeDiffGeneratorTest extends GroovyTestCase {

	def parser
	Definitions wsdl1
	Definitions wsdl2

	void setUp() {
		parser = new WSDLParser(resourceResolver : new ClasspathResolver())
		wsdl1 = parser.parse('diff/message-parts/BLZService1.wsdl')
		wsdl2 = parser.parse('diff/message-parts/BLZService2.wsdl')
	}

	void testPortTypeOperationChanges() {
		def diffs = new WsdlDiffGenerator(a: wsdl2, b: wsdl1).comparePortTypes()
		assert diffs*.dump().toString().contains('PortType BLZServicePortType:')
		assert diffs*.dump().toString().contains("Part parameters in the modified document uses an invalid element('getBank'):")
		assert diffs*.dump().toString().contains("Could not find the referenced element 'getBank' in namespace 'http://schemas.xmlsoap.org/wsdl/'.")
	}

	void testPortTypeNameAndOperationChanges() {
		wsdl2.portTypes[0].name = 'Test'
		def diffs = new WsdlDiffGenerator(a: wsdl2, b: wsdl1).comparePortTypes()
		assert diffs*.dump().toString().contains('PortType name has changed from Test to BLZServicePortType:')
		assert diffs*.dump().toString().contains("Part parameters in the modified document uses an invalid element('getBank'):")
		assert diffs*.dump().toString().contains("Could not find the referenced element 'getBank' in namespace 'http://schemas.xmlsoap.org/wsdl/'.")
	}
	
	void testPTNameChanges() {
		wsdl2 = parser.parse('diff/message-parts/BLZService1.wsdl')
		wsdl2.portTypes[0].name = 'Test'
		def diffs = new WsdlDiffGenerator(a: wsdl1, b: wsdl2).comparePortTypes()
		assert diffs[0].description == "PortType name has changed from BLZServicePortType to Test:"
		diffs = new WsdlDiffGenerator(a: wsdl2, b: wsdl1).comparePortTypes()
		assert diffs[0].description == "PortType name has changed from Test to BLZServicePortType:"
	}
	
	void testPTWithNoChangesButInvalidDocument() {
		//Because of the error in part there will be a diff, although the documents are the same!
		wsdl2 = parser.parse('diff/message-parts/BLZService1.wsdl')
		def diffs = new WsdlDiffGenerator(a: wsdl1, b: wsdl2).comparePortTypes()
		assert diffs*.dump().toString().contains("Could not find the referenced element 'getBank' in namespace 'http://schemas.xmlsoap.org/wsdl/'.")
		diffs = new WsdlDiffGenerator(a: wsdl1, b: wsdl1).comparePortTypes()
		assert diffs*.dump().toString().contains("Could not find the referenced element 'getBank' in namespace 'http://schemas.xmlsoap.org/wsdl/'.")
	}
}
