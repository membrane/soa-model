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
import com.predic8.soamodel.CreatorContext;

class SOARequestCreatorSOAP12Test extends GroovyTestCase{

  def request

  void testRequestTemplateCreatorWithDocLit(){
    def strWriter = new StringWriter()
    def creator = new SOARequestCreator(getDefinitions("BLZService.wsdl"), new RequestTemplateCreator(), new MarkupBuilder(strWriter))
    creator.createRequest('BLZServicePortType', 'getBank', 'BLZServiceSOAP12Binding')
    request = new XmlSlurper().parseText(strWriter.toString())
    assertEquals(Consts.SOAP12_NS, request.Envelope.lookupNamespace('s12'))
    assertEquals('?XXX?', request.Body.getBank.blz.text())
  }

  void testRequestCreatorWithDocLit(){
    def strWriter = new StringWriter()
    def formParams = [:]
    formParams['xpath:/getBank/blz']='37050198'
    def creator = new SOARequestCreator(getDefinitions("BLZService.wsdl"), new RequestCreator(), new MarkupBuilder(strWriter))
    creator.formParams = formParams
    creator.createRequest('BLZServicePortType', 'getBank', 'BLZServiceSOAP12Binding')
    request = new XmlSlurper().parseText(strWriter.toString())
    assertEquals(Consts.SOAP12_NS, request.Envelope.lookupNamespace('s12'))
    assertEquals('37050198', request.Body.getBank.blz.text())
  }

  private def getDefinitions(input) {
    def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
    parser.parse(new WSDLParserContext(input:input))
  }
}