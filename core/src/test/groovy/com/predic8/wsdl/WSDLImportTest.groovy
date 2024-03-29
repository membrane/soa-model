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

package com.predic8.wsdl

import groovy.namespace.QName as GQName
import com.predic8.xml.util.*
import com.predic8.wsdl.creator.*
import groovy.test.GroovyTestCase
import groovy.xml.MarkupBuilder
import groovy.xml.XmlSlurper

class WSDLImportTest extends GroovyTestCase{

  Definitions wsdl

  void setUp(){
    def parser = new WSDLParser( resourceResolver: new ClasspathResolver())
    wsdl = parser.parse("/import/stockquoteservice.wsdl")
  }

  void testNamespaces() {
    assert wsdl.imports.namespace.contains('http://example.com/stockquote/definitions')
  }

  void testGetPortTypeByQName() {
    assertNotNull(wsdl.getPortType(new GQName('http://example.com/stockquote/definitions', 'StockQuotePortType')))
    assertEquals(1, wsdl.registry.getWsdls('http://example.com/stockquote/definitions')[0].portTypes.size())
  }

  void testGetMessageByQName() {
    assertNotNull(wsdl.getMessage(new GQName('http://example.com/stockquote/messages', 'GetLastTradePriceInput')))
    assertEquals(2, wsdl.registry.getWsdls('http://example.com/stockquote/messages')[0].messages.size())
  }

  void testBindingByQName() {
    assertNotNull(wsdl.getBinding(new GQName('http://example.com/stockquote/service', 'StockQuoteSoapBinding')))
  }

  void testServiceByQName() {
    assertNotNull(wsdl.getService(new GQName('http://example.com/stockquote/service', 'StockQuoteService')))
  }

  void testCreator() {
    def strWriter = new StringWriter()
    def creator = new WSDLCreator(builder : new MarkupBuilder(strWriter))
    creator.createDefinitions(wsdl, new WSDLCreatorContext())
    def createdWSDL = new XmlSlurper().parseText(strWriter.toString())
  }

}

