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

import com.predic8.wsdl.*
import com.predic8.xml.util.*
import com.predic8.creator.*

class RequestTemplateCreatorTest extends GroovyTestCase {

  def definitions
  def operationName ='getBank'
  def portType = 'BLZServicePortType'

  void setUp() {
    definitions = getDefinitions()
  }

  void testElementRequestTemplate() {
    def operation = definitions.getOperation(operationName, portType)
    def element = definitions.getElementForOperation(operationName, portType)
    def requestTemplate = new XmlSlurper().parseText(element.requestTemplate)
    assertEquals('?XXX?', requestTemplate.blz.text())
    assertEquals('?999?', requestTemplate.@testAttribute.toString())
  }

  private def getDefinitions() {
    def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
    definitions = parser.parse(new WSDLParserContext(input:"/BLZService.wsdl"))
  }
}
