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

package com.predic8.wsdl.header

import groovy.xml.*
import com.predic8.wsdl.*
import com.predic8.wsdl.creator.*
import com.predic8.wstool.creator.*
import com.predic8.xml.util.*
import com.predic8.wsdl.soap11.*

class WSDLHeaderTest extends GroovyTestCase {

  def wsdl
  def createdWSDL

  void setUp(){
    def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
    wsdl = parser.parse(new WSDLParserContext(input:"/header/LibraryServiceService.wsdl"))
    def strWriter = new StringWriter()
    def creator = new WSDLCreator(builder : new MarkupBuilder(strWriter))
    creator.createDefinitions(wsdl, new WSDLCreatorContext())
    createdWSDL = new XmlSlurper().parseText(strWriter.toString())
  }

  void testParsing(){
    assertEquals(3, wsdl.getBinding('LibraryServicePortBinding').getOperation('addBook').input.bindingElements.size())
    assertEquals(2, wsdl.getBinding('LibraryServicePortBinding').getOperation('addBook').input.bindingElements.findAll{it instanceof SOAPHeader}.size())
  }

  void testWSDLCreator(){
    assertTrue(createdWSDL.binding.operation.input.body.@parts.toString().contains('parameters'))
    assertTrue(createdWSDL.binding.operation.input.header.@part.toString().contains('username'))
    assertTrue(createdWSDL.binding.operation.input.header.@part.toString().contains('timeout'))
  }

  void testFormCreator(){
    def strWriter = new StringWriter()
    def creator = new FormCreator(builder : new MarkupBuilder(strWriter), definitions : wsdl)
    creator.createRequest('LibraryService', 'addBook', 'LibraryServicePortBinding')
    strWriter = "<div>"+ strWriter + "</div>"
    def request = new XmlSlurper().parseText(strWriter.toString())
    assertTrue(request.table.tbody.tr.td.input.@name.toString().contains('username'))
    assertTrue(request.table.tbody.tr.td.input.@name.toString().contains('timeout'))
    assertTrue(request.div.table.tbody.tr.td.input.@name.toString().contains('title'))
    assertTrue(request.div.table.tbody.tr.td.input.@name.toString().contains('author'))
  }
}

