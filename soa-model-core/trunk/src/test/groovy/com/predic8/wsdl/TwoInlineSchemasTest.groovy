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

import groovy.xml.*

class TwoInlineSchemasTest extends AbstractWSDLTest{
  
  def schemaA
  def schemaB
  def static wsdl = '''
<definitions name="zweiSchema" targetNamespace="http://j2ee.netbeans.org/wsdl/zweiSchema"
    xmlns="http://schemas.xmlsoap.org/wsdl/"
    xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:tns="http://j2ee.netbeans.org/wsdl/zweiSchema" xmlns:plnk="http://docs.oasis-open.org/wsbpel/2.0/plnktype" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:ns0="urn:a" xmlns:ns1="urn:b">
    <types>
        <xsd:schema xmlns:xsd="http://www.w3.org/2001/XMLSchema"
        targetNamespace="urn:a"
        xmlns:b="urn:b"
        xmlns:tns="urn:a"
        elementFormDefault="qualified">
          <xsd:import namespace="urn:b"/>
            <xsd:complexType name="AType">
                <xsd:sequence>
                    <xsd:element name="a" type="xsd:string"/>
        </xsd:sequence>
      </xsd:complexType>
      <xsd:element name="aa" type="tns:AType"/>
      <xsd:element name="dd" type="b:BType"/>
    </xsd:schema>
    <xsd:schema xmlns:xsd="http://www.w3.org/2001/XMLSchema"
        targetNamespace="urn:b"
        xmlns:tns="urn:b"
        xmlns:a="urn:a"
        elementFormDefault="qualified">
        <xsd:import namespace="urn:a"/>
      <xsd:complexType name="BType">
        <xsd:sequence>
          <xsd:element name="b" type="xsd:string"/>
        </xsd:sequence>
      </xsd:complexType>
      <xsd:element name="bb" type="tns:BType"/>
      <xsd:element name="cc" type="a:AType"/>
    </xsd:schema>
  </types>
</definitions>'''

  void setUp() {
    super.setUp()
    schemaA = definitions.schemas.find{ it.targetNamespace == 'urn:a'}
    schemaB = definitions.schemas.find{ it.targetNamespace == 'urn:b'}
  }
  
  void testGetElement() {
    assertNotNull(definitions.getElement('aa'))
    assertNotNull(definitions.getElement('bb'))
  }
  
  void testElementType() {
    assertEquals(new QName('urn:a','AType'), (definitions.getElement('aa').type))
    assertEquals(new QName('urn:b','BType'), (definitions.getElement('bb').type))
  }
  
  void testFindTypeInImportedSchema() {
    assertEquals(new QName('urn:a','AType'), schemaB.getType(schemaB.getElement('cc').type).qname) 
  }
  
  void testFindTypeInImportedSchemabBeforeDeclaration() {
    assertEquals(new QName('urn:b','BType'), schemaA.getType(schemaA.getElement('dd').type).qname) 
  }
}
