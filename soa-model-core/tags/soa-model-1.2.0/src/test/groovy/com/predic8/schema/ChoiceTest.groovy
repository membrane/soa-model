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

import javax.xml.stream.*
import groovy.xml.*

import com.predic8.xml.util.*
import com.predic8.schema.diff.*
import com.predic8.schema.creator.*
import com.predic8.wstool.creator.*

class ChoiceTest extends GroovyTestCase{
	
  public static HR_NS = "http://predic8.com/human-resources/"
  public static CONT_INF = new QName(HR_NS,"ContactInfoType")

  def schema
  
  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse(input:"/schema/choice/choice.xsd")
  }

  void testChoiceInComplexType(){
    def choice = schema.getType(CONT_INF).model//.elements.find{ it.name == 'person'}.embeddedType.model
    assertNotNull(choice)
  }

  void testSchemaCreator(){
    def strWriter = new StringWriter()
    def creator = new SchemaCreator(builder : new MarkupBuilder(strWriter))
    schema.create(creator, new SchemaCreatorContext())
    def testSchema = new XmlSlurper().parseText(strWriter.toString())
    assertEquals(2, testSchema.complexType.choice.sequence.element.size())
    assertEquals(3, testSchema.complexType.choice.element.size())
    assertEquals(1, testSchema.complexType.choice.sequence.size())
    
    assertEquals('fax', testSchema.complexType.choice.element[2].@name.toString())
  }

  void testRequestCreator() {
  def strWriter = new StringWriter()
  def creator = new RequestCreator(builder : new MarkupBuilder(strWriter))
  schema.getElement('contactInfo').create(creator, new RequestCreatorContext(formParams: ["xpath:/contactInfo/phone":"+49 228 2402099", "xpath:/contactInfo/city":"Bonn"]))
  def inf = new XmlSlurper().parseText(strWriter.toString())
  assertEquals('+49 228 2402099', inf.phone.toString())
  }

  /*void testRequestTemplateCreator() {
  def strWriter = new StringWriter()
  def creator = new RequestTemplateCreator(builder : new MarkupBuilder(strWriter))
  schema.getElement('employee').create(creator, [:])
  def emp = new XmlSlurper().parseText(strWriter.toString())
  assertEquals('?XXX?', emp.person.firstName.toString())
  assertEquals('?XXX?', emp.person.lastName.toString())
  }*/
}

