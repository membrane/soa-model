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

package com.predic8.wsdl.creator;


import groovy.xml.*

import com.predic8.wsdl.*
import com.predic8.xml.util.*
import com.predic8.creator.*

class BLZCreatorTest extends GroovyTestCase {

  def definitions
  def createdWSDL

  void setUp() {
    definitions = getDefinitions()
    def strWriter = new StringWriter()
    def creator = new WSDLCreator(builder : new MarkupBuilder(strWriter))
    creator.createDefinitions(definitions, new WSDLCreatorContext())
    createdWSDL = new XmlSlurper().parseText(strWriter.toString())
  }

  private def getDefinitions() {
    def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
    definitions = parser.parse(new WSDLParserContext(input:"/BLZService.wsdl"))
  }

  void testCreator() {
    assertEquals('BLZService', createdWSDL.toString())
  }
}