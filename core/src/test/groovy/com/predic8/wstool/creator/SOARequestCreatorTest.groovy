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

package com.predic8.wstool.creator

import groovy.xml.*
import com.predic8.wsdl.*
import com.predic8.xml.util.*
import com.predic8.soamodel.Consts

class SOARequestCreatorTest extends GroovyTestCase{
  
  def req
  
  void testRequestWithEmptyCT(){
    def sw = new StringWriter()
    new SOARequestCreator(getDefinitions("/ArticleService/ArticleService.wsdl"), new RequestCreator(),new MarkupBuilder(sw)).createRequest 'ArticleServicePT', 'getAll', 'ArticleServicePTBinding'
    assertEquals(Consts.SOAP11_NS, getRequest(sw).Envelope.lookupNamespace('s11'))
    assertEquals('', getRequest(sw).Body.getAll.text())
  }
  
  void testRequestWithEmptyElement(){
    def sw = new StringWriter()
    def creator = new SOARequestCreator(getDefinitions("/ArticleService/ArticleService2.wsdl"), new RequestCreator(), new MarkupBuilder(sw))
    creator.formParams = ['xpath:/getAll':'']
    creator.createRequest 'ArticleServicePT', 'getAll', 'ArticleServicePTBinding'
    assertEquals(Consts.SOAP11_NS, getRequest(sw).Envelope.lookupNamespace('s11'))
    assertEquals('', getRequest(sw).Body.getAll.text())
  }
  
  void testRequest(){
    def sw = new StringWriter()
    def creator = new SOARequestCreator(getDefinitions("/ArticleService/ArticleService.wsdl"),
        new RequestTemplateCreator(),
        new MarkupBuilder(sw))
    creator.maxRecursionDepth = 10
    creator.createRequest 'ArticleServicePT', 'get', 'ArticleServicePTBinding'
      assertEquals('???', getRequest(sw).Body.get.id.text())
  }
  
  private getRequest(sw) {
    new XmlSlurper().parseText(sw.toString())
  }
  
  private def getDefinitions(input) {
    def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
    parser.parse(new WSDLParserContext(input:input))
  }
}