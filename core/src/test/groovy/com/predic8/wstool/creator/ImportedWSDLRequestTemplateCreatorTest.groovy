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

package com.predic8.wstool.creator;

import groovy.xml.*
import com.predic8.creator.*
import com.predic8.schema.Element
import com.predic8.wsdl.*
import com.predic8.xml.util.*

class ImportedWSDLRequestTemplateCreatorTest extends GroovyTestCase {

	WSDLParser parser = new WSDLParser(resourceResolver: new ClasspathResolver())
	Definitions definitions = parser.parse("import/stockquoteservice.wsdl")
	
	def portTypePrefixedName = new PrefixedName('defs:StockQuotePortType')
	QName portType = new QName(definitions.getNamespace(portTypePrefixedName.prefix),portTypePrefixedName.localName)
	def operationName ='GetLastTradePrice'

	void setUp() {
		definitions = getDefinitions()
	}

	void testElementRequestTemplate() {
		Element element = definitions.getInputElementForOperation(portType.localPart, operationName)
		def requestTemplate = new XmlSlurper().parseText(element.requestTemplate)
		assert requestTemplate.text() == '?999?' 
	}
	
}
