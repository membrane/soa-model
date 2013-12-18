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

class DocumentationDiffGeneratorTest {

	Definitions wsdl1 //original wsdl
	Definitions wsdl2 //local wsdl with documentation

    @Before
	void setUp() {
        Assume.assumeTrue(!System.getenv('OFFLINETESTING'))
		def parser = new WSDLParser()
		wsdl1 = parser.parse('http://www.thomas-bayer.com/axis2/services/BLZService?wsdl')
		parser.resourceResolver = new ClasspathResolver()
		wsdl2 = parser.parse("BLZService-with-documentation.wsdl")
	}

    @Test
	void testDocumentationInDefinitions() {
		def diffs = compare(wsdl1, wsdl2)
		assert 6 == diffs*.dump().toString().count('Documentation added.')
		assert 1 == diffs*.dump().toString().count('Documentation has changed.')
	}

	private def compare(a, b) {
		new WsdlDiffGenerator(a: a, b: b).compare()
	}
}
