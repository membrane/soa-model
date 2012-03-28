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

package com.predic8.schema

import junit.framework.TestCase
import javax.xml.stream.*

import groovy.xml.*

class SchemaTest extends GroovyTestCase{
  
  def token
  def schema1
  def schema2
  def static qname = new QName("http://thomas-bayer.com/blz/",'getBankType')
    
  def static xsdNonPrefixed = '''<xsd:schema attributeFormDefault="unqualified" elementFormDefault="qualified" xmlns:xsd="http://www.w3.org/2001/XMLSchema" targetNamespace="http://thomas-bayer.com/blz/">
                                   <xsd:complexType name="getBankType">
                                     <xsd:sequence>
                                       <xsd:element name="blz" type="xsd:string">
                                        <xsd:annotation>
                                          <xsd:documentation>test Document</xsd:documentation>
                                        </xsd:annotation>
                                       </xsd:element>
                                     </xsd:sequence>
                                   </xsd:complexType>
                                 </xsd:schema>'''
                          
  def static xsdPrefixed = '''<xsd:schema attributeFormDefault="unqualified" elementFormDefault="qualified" xmlns:xsd="http://www.w3.org/2001/XMLSchema" targetNamespace="http://thomas-bayer.com/blz/" xmlns:tns="http://thomas-bayer.com/blz/">
                                <xsd:complexType name="tns:getBankType">
                                  <xsd:annotation>
                                    <xsd:documentation>Comp Docu</xsd:documentation>
                                  </xsd:annotation>
                                  <xsd:sequence>
                                    <xsd:element name="blz" type="xsd:string"/>
                                  </xsd:sequence>
                                  <attribute name="country" type="xsd:string" />
                                </xsd:complexType>
                              </xsd:schema>'''
  
  void setUp() {
    token = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xsdNonPrefixed))
    while (token.hasNext()) {
      if(token.startElement) {
        if(token.name.getLocalPart() == 'schema') {
          schema1 = new Schema()
          schema1.parse(token, [:])
        }
      }
      if(token.hasNext()) token.next()
    }
    token = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xsdPrefixed))
    while (token.hasNext()) {
      if(token.startElement) {
        if(token.name.getLocalPart() == 'schema') {
          schema2 = new Schema()
          schema2.parse(token, [:])
        }
      }
      if(token.hasNext()) token.next()
    }
  }
  
  void testSchema() {
    assertEquals("http://thomas-bayer.com/blz/" , schema1.targetNamespace)
  }
  
  void testElementDocumentation() {
    assertEquals('test Document' , schema1.getType('getBankType').sequence.particles[0].annotation.documentations[0].content.toString())
  }
  
  void testComplexTypeDocumentation() {
    assertEquals('Comp Docu', schema2.getType('getBankType').annotation.documentations[0].content)
  }
  
  void testNotPrefixedComplexTypeName(){
    assertEquals(qname, schema1.complexTypes[0].qname)
  }
  
  void testPrefixedComplexTypeName(){
    assertEquals(qname, schema2.complexTypes[0].qname)
  }
  
  void testComplesType() {
    assertEquals(1 , schema1.complexTypes[0].sequence.particles.size())
    assertEquals("blz" , schema1.complexTypes[0].sequence.particles[0].name)
  }
  
  void testAttributeInComplesType() {
    assertEquals('country', schema2.getType('getBankType').attributes[0].name)
  }
}
