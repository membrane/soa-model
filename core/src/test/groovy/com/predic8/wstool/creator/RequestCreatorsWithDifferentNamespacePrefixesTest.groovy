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

class RequestCreatorsWithDifferentNamespacePrefixesTest extends GroovyTestCase{

  def request

  void testRequestTemplateCreator(){
    def sw = new StringWriter()
    def creator = new SOARequestCreator(getDefinitions("/ns-test.wsdl"), new RequestTemplateCreator(), new MarkupBuilder(sw))
    creator.createRequest("MYTESTServicePortType", "MYTEST", "MYTESTServiceBinding")
    request = parseRequest(sw)
    assertEquals(Consts.SOAP11_NS, request.Envelope.lookupNamespace('s11'))
    assertEquals('http://www.test.com/mytest/reoperation', request.Body.MYTESTRequestDocument.lookupNamespace('TEST3N1'))
    assertEquals('http://www.test.com/mytest/reoperation/types', request.Body.MYTESTRequestDocument.t1.lookupNamespace('ns1'))
  }

  void testRequestCreator(){
    def sw = new StringWriter()
    def formParams = [:]
    formParams['xpath:/MYTESTRequestDocument/t1']='foo'
    formParams['xpath:/MYTESTRequestDocument/t2']='bar'
    def creator = new SOARequestCreator(getDefinitions("/ns-test.wsdl"), new RequestCreator(), new MarkupBuilder(sw))
    creator.formParams = formParams
    creator.createRequest("MYTESTServicePortType", "MYTEST", "MYTESTServiceBinding")
    request = parseRequest(sw)
		assertEquals(Consts.SOAP11_NS, request.Envelope.lookupNamespace('s11'))
		assertEquals('http://www.test.com/mytest/reoperation', request.Body.MYTESTRequestDocument.lookupNamespace('TEST3N1'))
		assertEquals('http://www.test.com/mytest/reoperation/types', request.Body.MYTESTRequestDocument.t1.lookupNamespace('ns1'))
    assertEquals('foo', request.Body.MYTESTRequestDocument.t1.text())
    assertEquals('bar', request.Body.MYTESTRequestDocument.t2.text())
  }

  def parseRequest(sw) {
    new XmlSlurper().parseText(sw.toString())
  }
  
  private def getDefinitions(input) {
    def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
    parser.parse(input)
  }
}