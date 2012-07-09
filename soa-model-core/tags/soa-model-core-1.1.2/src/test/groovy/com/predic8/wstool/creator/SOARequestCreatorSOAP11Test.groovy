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

class SOARequestCreatorSOAP11Test extends GroovyTestCase{

  def request

  void testRequestTemplateCreatorWithDocLit(){
    def sw = new StringWriter()
    def creator = new SOARequestCreator(getDefinitions("/header/LibraryServiceService.wsdl"), new RequestTemplateCreator(), new MarkupBuilder(sw))
    creator.createRequest('LibraryService', 'addBook', 'LibraryServicePortBinding')
    request = getRequest(sw)
    assertEquals(Consts.SOAP11_NS, request.Envelope.lookupNamespace('s11'))
    assertEquals('?XXX?', request.Header.username.text())
    assertEquals('?999?', request.Header.timeout.text())
    assertEquals('?XXX?', request.Body.addBook.title.text())
    assertEquals('?XXX?', request.Body.addBook.author.text())
  }

  void testRequestCreatorWithDocLit(){
    def sw = new StringWriter()
    def formParams = [:]
    formParams['xpath:/username']='Malkhas'
    formParams['xpath:/timeout']='200'
    formParams['xpath:/addBook/title']='MyBook'
    formParams['xpath:/addBook/author']='Me'
    def creator = new SOARequestCreator(getDefinitions("/header/LibraryServiceService.wsdl"), new RequestCreator(), new MarkupBuilder(sw))
    creator.formParams = formParams
    creator.createRequest('LibraryService', 'addBook', 'LibraryServicePortBinding')
    request = getRequest(sw)
    assertEquals(Consts.SOAP11_NS, request.Envelope.lookupNamespace('s11'))
    assertEquals('Malkhas', request.Header.username.text())
    assertEquals('200', request.Header.timeout.text())
    assertEquals('MyBook', request.Body.addBook.title.text())
    assertEquals('Me', request.Body.addBook.author.text())
  }

  void testRequestTemplateCreatorWithRPCLit(){
    def sw = new StringWriter()
    def creator = new SOARequestCreator(getDefinitions("/RPCLiteralSample.wsdl"), new RequestTemplateCreator(), new MarkupBuilder(sw))
    creator.createRequest('RPCLiteralSamplePT', 'addPerson', 'RPCLiteralSampleBinding')
    request = getRequest(sw)
    assertEquals(Consts.SOAP11_NS, request.Envelope.lookupNamespace('s11'))
    assertEquals('?XXX?', request.Body.addPerson.name.text())
    assertEquals('?XXX?', request.Body.addPerson.lastname.text())
    assertEquals('?XXX?', request.Body.addPerson.email.text())
    assertEquals('?999?', request.Body.addPerson.age.text())
  }

  void testRequestCreatorWithRPCLit(){
    def sw = new StringWriter()
    def formParams = [:]
    formParams['xpath:/addPerson/name']='kaveh'
    formParams['xpath:/addPerson/lastname']='keshavarzi'
    formParams['xpath:/addPerson/age']='28'
    formParams['xpath:/addPerson/email']='foo@bar'
    def creator = new SOARequestCreator(getDefinitions("/RPCLiteralSample.wsdl"), new RequestCreator(), new MarkupBuilder(sw))
    creator.formParams = formParams
    creator.createRequest('RPCLiteralSamplePT', 'addPerson', 'RPCLiteralSampleBinding')
    request = getRequest(sw)
    assertEquals(Consts.SOAP11_NS, request.Envelope.lookupNamespace('s11'))
    assertEquals('kaveh', request.Body.addPerson.name.text())
    assertEquals('keshavarzi', request.Body.addPerson.lastname.text())
    assertEquals('28', request.Body.addPerson.age.text())
    assertEquals('foo@bar', request.Body.addPerson.email.text())
  }


  void testEnvelopeWrapper(){
    def sw = new StringWriter()
    def creator = new SOARequestCreator(getDefinitions("/RPCLiteralSample.wsdl"),
                            new RequestTemplateCreator(),
                            new MarkupBuilder(sw))
    creator.bindingName = 'RPCLiteralSampleBinding'
    creator.operationName = 'addPerson'
    creator.wrapEnvelope('<addPerson><name>Shaan</name></addPerson>')
    assertEquals(Consts.SOAP11_NS, getRequest(sw).Envelope.lookupNamespace('s11'))
    assertEquals('Shaan', getRequest(sw).Body.addPerson.name.text())
  }

  private getRequest(sw) {
    new XmlSlurper().parseText(sw.toString())
  }
  
  private def getDefinitions(input) {
    def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
    parser.parse(new WSDLParserContext(input:input))
  }
}