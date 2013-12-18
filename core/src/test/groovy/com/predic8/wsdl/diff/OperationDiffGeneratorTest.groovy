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
import org.junit.Assume
import org.junit.Before
import org.junit.Test

class OperationDiffGeneratorTest {

	Definitions wsdl1 
	Definitions wsdl2 

    @Before
	void setUp() {
        Assume.assumeTrue(!System.getenv('OFFLINETESTING'))
		def parser = new WSDLParser()
		wsdl1 = parser.parse('http://www.thomas-bayer.com/axis2/services/BLZService?wsdl')
		parser.resourceResolver = new ClasspathResolver()
		wsdl2 = parser.parse("BLZService-with-documentation.wsdl")
	}

    @Test
	void testOperationInput() {
		def diffs = compare(wsdl1, wsdl2)
		assert diffs[0].diffs[1].diffs[1].diffs[1].description == 'Input:'
		assert diffs[0].diffs[1].diffs[1].diffs[1].safe()
		assert diffs[0].diffs[1].diffs[1].diffs[1].diffs[0].description == 'Name has changed from null to getBankInput.'
		assert diffs[0].diffs[1].diffs[1].diffs[1].diffs[0].safe()
		assert diffs[0].diffs[1].diffs[1].diffs[1].diffs[1].description == 'Message getBank:'
		assert !diffs[0].diffs[1].diffs[1].diffs[1].diffs[1].breaks()
		assert diffs[0].diffs[1].diffs[1].diffs[1].diffs[1].diffs[0].description == 'Documentation added.'
		assert !diffs[0].diffs[1].diffs[1].diffs[1].diffs[1].diffs[0].breaks()
	}

    @Test
	void testOperationOutput() {
		def diffs = compare(wsdl1, wsdl2)
		assert diffs[0].diffs[1].diffs[1].diffs[2].description == 'Output:'
		assert diffs[0].diffs[1].diffs[1].diffs[2].breaks()
		assert !diffs[0].diffs[1].diffs[1].diffs[2].safe()
		assert diffs[0].diffs[1].diffs[1].diffs[2].diffs[0].description == 
		'Message has changed from {http://thomas-bayer.com/blz/}getBankResponse to {http://thomas-bayer.com/blz/}getBankResponseTEST.'
		assert diffs[0].diffs[1].diffs[1].diffs[2].diffs[0].safe()
		assert diffs[0].diffs[1].diffs[1].diffs[2].diffs[1].description == 'Message:'
		assert diffs[0].diffs[1].diffs[1].diffs[2].diffs[1].breaks()
		assert diffs[0].diffs[1].diffs[1].diffs[2].diffs[1].diffs[0].diffs[0].description == 
		'Element has changed from tns:getBankResponse to tns:getBank.'
		assert !diffs[0].diffs[1].diffs[1].diffs[2].diffs[1].diffs[0].breaks 
		assert diffs[0].diffs[1].diffs[1].diffs[2].diffs[1].diffs[0].diffs[0].breaks 
	}

	private def compare(a, b) {
		new WsdlDiffGenerator(a: a, b: b).compare()
	}
}
