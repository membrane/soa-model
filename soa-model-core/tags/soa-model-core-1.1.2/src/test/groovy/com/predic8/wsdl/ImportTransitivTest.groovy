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

class ImportTransitivTest extends AbstractWSDLTest{
  
  def schemaA
  def schemaB
  def schemaC
  def static wsdl = '''
<definitions name="zweiSchema" targetNamespace="http://j2ee.netbeans.org/wsdl/zweiSchema"
    xmlns="http://schemas.xmlsoap.org/wsdl/"
    xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:tns="http://j2ee.netbeans.org/wsdl/zweiSchema" xmlns:plnk="http://docs.oasis-open.org/wsbpel/2.0/plnktype" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:ns0="urn:a" xmlns:ns1="urn:b">
    <types>
        <xsd:schema xmlns:xsd="http://www.w3.org/2001/XMLSchema"
        targetNamespace="urn:a"
        xmlns:tns="urn:a"
        elementFormDefault="qualified">
            <xsd:complexType name="AType">
                <xsd:sequence>
                    <xsd:element name="a" type="xsd:string"/>
        </xsd:sequence>
      </xsd:complexType>
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
    </xsd:schema>
    <xsd:schema xmlns:xsd="http://www.w3.org/2001/XMLSchema"
        targetNamespace="urn:c"
        xmlns:tns="urn:c"
        xmlns:a="urn:a"
        xmlns:b="urn:b"
        elementFormDefault="qualified">
        <xsd:import namespace="urn:b"/>
      <xsd:element name="aa" type="a:AType"/>
      <xsd:element name="bb" type="b:BType"/>
    </xsd:schema>
  </types>
</definitions>'''

  void setUp() {
    super.setUp()
    schemaA = definitions.schemas.find{ it.targetNamespace == 'urn:a'}
    schemaB = definitions.schemas.find{ it.targetNamespace == 'urn:b'}
    schemaC = definitions.schemas.find{ it.targetNamespace == 'urn:c'}
  }
  
  void testGetElement() {
//    definitions.schemas.elements.each {
//    }
  }
  
  void testFindTypeInImportedSchemaFromAnImportedSchema() {
    assertEquals('AType', schemaC.getType(schemaC.getElement('aa').type).qname.getLocalPart())
    assertEquals('BType', schemaC.getType(schemaC.getElement('bb').type).qname.getLocalPart())
  }
}
