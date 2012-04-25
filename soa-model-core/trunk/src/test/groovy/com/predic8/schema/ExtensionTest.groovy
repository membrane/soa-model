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

import javax.xml.xpath.*
import groovy.xml.*
import com.predic8.schema.creator.*
import com.predic8.wstool.creator.*
import com.predic8.xml.util.ClasspathResolver

class ExtensionTest extends GroovyTestCase{
  
  def schema
  
  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse(input:"/schema/ExtensionTest.xsd")
  }
  
  void testParsing() {
    assertEquals(new QName('http://www.example.com/IPO', 'BaseAddress'), schema.getType('Address').model.derivation.base)
    assertEquals(new QName('http://www.example.com/IPO', 'Address'), schema.getType('USAddress').model.derivation.base)
    assertEquals(2, schema.getType('USAddress').model.derivation.model.particles.size())
    
  }

  void testExtensionAttributes(){
    assertEquals('USAddressAttribute', schema.getType('USAddress').model.derivation.attributes[0].name)
  }
  
  void testSchemaCreator() {
    def strWriter = new StringWriter()
    def creator = new SchemaCreator(builder : new MarkupBuilder(strWriter))
    schema.create(creator, new SchemaCreatorContext())
    def testSchema = new XmlSlurper().parseText(strWriter.toString())
    assertEquals("ipo:BaseAddress", testSchema.complexType[1].complexContent.extension.@base.toString())
    assertEquals(3, testSchema.complexType.complexContent.extension.sequence.size())
    assertEquals(1, testSchema.complexType[3].complexContent.extension.attribute.size())
  }

  void testSchemaCreatorCreateLinks() {
    def strWriter = new StringWriter()
    def creator = new SchemaCreator(builder : new MarkupBuilder(strWriter))
    schema.create(creator, new SchemaCreatorContext(createLinks:true))
    def str = creator.escapeMarkup(strWriter.toString())
  }

  void testRequestTemplateCreator() {
    def strWriter = new StringWriter()
    def creator = new RequestTemplateCreator(builder : new MarkupBuilder(strWriter))
    schema.getElement('MyAddress').create(creator, new RequestTemplateCreatorContext())
    assertTrue(strWriter.toString().contains('<!-- This element can be extended by any attribute from ##any namespace -->'))
    def testXML = new XmlSlurper().parseText(strWriter.toString())
    assertEquals(6, testXML.children().size())
    def elementsNames =[]
    testXML.childNodes().each{ elementsNames << it.name() }
    assertEquals(["name", "street", "number", "city", "state", "zip"], elementsNames)
  }

  void testFormCreator() {
    def strWriter = new StringWriter()
    def creator = new FormCreator(builder : new MarkupBuilder(strWriter))
    schema.getElement('MyAddress').create(creator, new FormCreatorContext(formParams:'',path:"xpath:/"))
    def testXML = new XmlSlurper().parseText(strWriter.toString())
    def dom = DOMBuilder.parse(new StringReader(strWriter.toString()))
    def nodeList = XPathFactory.newInstance().newXPath().evaluate('//input', dom.documentElement, XPathConstants.NODESET)
    assertEquals(9, nodeList.length)
    def elementsNames =[]
    for(int i=0; i < nodeList.length; i++) {
      elementsNames << nodeList.item(i).getAttributes().getNamedItem('name').getNodeValue()
    }
    assertEquals(['xpath:/MyAddress/name', 'xpath:/MyAddress/street', 'xpath:/MyAddress/number', 'xpath:/MyAddress/@BaseAddressAttribute', 'xpath:/MyAddress/city', 'xpath:/MyAddress/@AddressAttribute', 'xpath:/MyAddress/state', 'xpath:/MyAddress/zip', 'xpath:/MyAddress/@USAddressAttribute'], elementsNames)
  }
  
  void testRequestCreator() {
    def strWriter = new StringWriter()
    def creator = new RequestCreator(builder : new MarkupBuilder(strWriter))
    def formParams = [:]
    formParams['xpath:/MyAddress/name']='predic8'
    formParams['xpath:/MyAddress/street']='B9'
    formParams['xpath:/MyAddress/number']='40'
    formParams['xpath:/MyAddress/city']='Bonn'
    formParams['xpath:/MyAddress/state']='NRW'
    formParams['xpath:/MyAddress/zip']='53177'
    schema.getElement('MyAddress').create(creator, new RequestCreatorContext(formParams:formParams, path:"xpath:/"))
    def testXML = new XmlSlurper().parseText(strWriter.toString())
    assertEquals(6, testXML.children().size())
    assertEquals('predic8', testXML.name.text())
    assertEquals('B9', testXML.street.text())
    assertEquals('40', testXML.number.text())
    assertEquals('Bonn', testXML.city.text())
    assertEquals('NRW', testXML.state.text())
    assertEquals('53177', testXML.zip.text())
  }

  void testAttribute() {
    assertEquals('BaseAddressAttribute', schema.getType('BaseAddress').attributes[0].name)
  }
}