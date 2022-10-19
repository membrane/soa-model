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
import groovy.test.GroovyTestCase

class SchemaDiffsInWSDL extends GroovyTestCase {

	Definitions original
	Definitions modified

	void setUp() {
		def parser = new WSDLParser(resourceResolver : new ClasspathResolver())
		original = parser.parse('diff/ArticleService-Original/ArticleService.wsdl')
		modified = parser.parse('diff/ArticleService-Modified/ArticleService.wsdl')
	}

	void testSchemaDiffsInDefinitions() {
		def diffs = compare(original, modified)
		assert diffs[0].diffs.size() == 4
		assert diffs[0].diffs[0].type == 'portType'
		assert diffs[0].diffs[1].type == 'types'
		assert diffs[0].diffs[0].diffs[0].description == 'Operation create:'
		assert diffs[0].diffs[0].diffs[1].description == 'Operation get:'
		assert diffs[0].diffs[0].diffs[2].description == 'Operation getAll:'
	}
	
	private def compare(a, b) {
		new WsdlDiffGenerator(a: a, b: b).compare()
	}
}
