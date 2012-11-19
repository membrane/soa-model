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

import javax.xml.stream.*

class WSDLTest extends AbstractWSDLTest {
  
  def binding
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
                           <documentation>WSDL Docu</documentation>
                       </wsdl:definitions>'''
  
  void testDefinitions() {
    assertEquals('BLZService' , definitions.name)
    assertEquals("http://thomas-bayer.com/blz/" , definitions.targetNamespace)
  }
  
  void testGetMessage() {
    def input = new Message(name : 'getBank')
    definitions.messages << input
    assertEquals('getBank' , definitions.getMessage('getBank').name)
  }
  
  void testNamespaces() {
    assertEquals(9 , definitions.namespaceContext.size())
    assertEquals("http://www.w3.org/2001/XMLSchema" , definitions.getNamespace('xsd'))
  }
  
  void testDocumentation() {
    assertEquals('WSDL Docu', definitions.documentation.toString())
  }
}
