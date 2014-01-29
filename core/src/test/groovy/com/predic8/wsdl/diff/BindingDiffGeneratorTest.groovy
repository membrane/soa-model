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

class BindingDiffGeneratorTest {

	Definitions original
	Definitions modified

    @Before
	void setUp() {
		def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
		original = parser.parse('diff/ArticleService-Original/ArticleService.wsdl')
		modified = parser.parse('diff/ArticleService-Modified/ArticleService.wsdl')
	}

    @Test
	void testBindingDiffs() {
		def diffs = new WsdlDiffGenerator(a: original, b: modified).compareBindings()
		assert diffs.size() == 2
		assert diffs[0].description == 'Binding TestBinding added.'
		assert diffs[1].description == 'Binding ArticleServicePTBinding changed:'
		assert diffs[1].diffs[0].description == "Style changed from 'Document/Literal-Wrapped' to 'Rpc/Literal'"
	}

}
