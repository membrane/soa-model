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

import junit.framework.TestCase
import javax.xml.stream.*

class PortTypeTest extends GroovyTestCase{
  
  def definitions
  def portType
  def token
  
  def static wsdl = '''<wsdl:definitions xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/" 
                                         xmlns="http://schemas.xmlsoap.org/wsdl/" 
                                         xmlns:wsaw="http://www.w3.org/2006/05/addressing/wsdl" 
                                         xmlns:tns="http://thomas-bayer.com/blz/" 
                                         xmlns:http="http://schemas.xmlsoap.org/wsdl/http/" 
                                         xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
                                         xmlns:mime="http://schemas.xmlsoap.org/wsdl/mime/" 
                                         xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" 
                                         xmlns:soap12="http://schemas.xmlsoap.org/wsdl/soap12/" 
                                         name="BLZService" 
                                         targetNamespace="http://thomas-bayer.com/blz/">
    <wsdl:portType name="BLZServicePortType">
      <documentation>PortType Docu</documentation>
      <wsdl:operation name="getBank">
        <documentation>Operation Docu</documentation>
        <wsdl:input message="tns:getBank"/>
        <wsdl:output message="tns:getBankResponse" />
      </wsdl:operation>
      <wsdl:operation name="getBankDetails">
        <wsdl:input message="tns:getBankDetails"/>
        <wsdl:output message="tns:getBankDetailsResponse" />
      </wsdl:operation>
    </wsdl:portType>
  </wsdl:definitions>'''
  
  void setUp() {
    def input = new Message(name : 'getBank')
    def output = new Message(name : 'getBankResponse')
    definitions = new Definitions()
    definitions.messages << input << output
    
    token = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(wsdl))
    while (token.hasNext()) {
      if(token.startElement) {
        if(token.name.getLocalPart() == 'portType') {
          portType = new PortType(definitions: definitions)
          definitions.portTypes << portType
          portType.parse(token, [:])
        }
      }
      if(token.hasNext()) token.next()
    }
  }
  
  void testOperations() {
    assertEquals(2 , portType.operations.size())
  }
  
  void testMessages() {
    assertNotNull(portType.operations[0].input)
    assertNotNull(portType.operations[0].output)
  }
  
  void testPortTypeDocumentation() {
      assertEquals('PortType Docu', portType.documentation.toString())
    }
    
    void testOperationDocumentation() {
    assertEquals('Operation Docu', definitions.getOperation('getBank','BLZServicePortType').documentation.toString())
    }
}
