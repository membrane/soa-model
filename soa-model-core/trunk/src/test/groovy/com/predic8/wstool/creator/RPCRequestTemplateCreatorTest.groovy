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

import junit.framework.TestCase
import groovy.xml.*

import com.predic8.wsdl.AbstractWSDLTest

class RPCRequestTemplateCreatorTest extends AbstractWSDLTest {
  
  def static wsdl = '''<wsdl:definitions name="urn:NewsSearch" targetNamespace="urn:NewsSearch" xmlns:typens="urn:NewsSearch" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/" xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/" xmlns="http://schemas.xmlsoap.org/wsdl/">
                         
                         <message name="doNewsSearch">
                           <part name="username" type="xsd:string"/>
                           <part name="password" type="xsd:string"/>
                           <part name="query" type="xsd:string"/>
                         </message>

                         <message name="doNewsSearchResponse">
                         </message>
                         
                         <portType name="NewsSearchPort">
                           <operation name="doNewsSearch">
                             <input message="typens:doNewsSearch"/>
                             <output message="typens:doNewsSearchResponse"/>
                           </operation>
                         </portType>
                       </wsdl:definitions>'''
  
                       
  void testDocumentation() {
//    def strWriter = new StringWriter()
//    def creator = new RPCRequestTemplateCreator(builder : new MarkupBuilder(strWriter),definitions: definitions)
//    creator.createRequest('NewsSearchPort', 'doNewsSearch')
//    def testXML = new XmlSlurper().parseText(strWriter.toString())
//    assertEquals(3, testXML.children().size())
//     def elementsNames =[]
//    testXML.childNodes().each{ elementsNames << it.name() }
//    assertEquals(["username", "password", "query"], elementsNames)
  }
}
