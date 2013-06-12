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

class PartWithTypeDiffGeneratorTest extends GroovyTestCase {

	Definitions wsdl1 
	Definitions wsdl2 

	void setUp() {
		def parser = new WSDLParser()
		parser.resourceResolver = new ClasspathResolver()
		wsdl1 = parser.parse('diff/part-with-type/original.wsdl')
		wsdl2 = parser.parse('diff/part-with-type/modified.wsdl')
	}

	void testPartTypeChanges() {
		def diffs = compare(wsdl1, wsdl2)
		assert diffs*.dump().toString().contains('ModelGroup has changed from null to All.')
		assert diffs*.dump().toString().contains('Type has changed from {http://www.w3.org/2001/XMLSchema}string to {http://www.w3.org/2001/XMLSchema}anySimpleType.')
		assert diffs*.dump().toString().contains('Type has changed from {urn:PessoaJuridicaSimplificada}Serialization to {urn:PessoaFisicaSimplificada}Serialization.')
	}

	private def compare(a, b) {
		new WsdlDiffGenerator(a: a, b: b).compare()
	}
}
