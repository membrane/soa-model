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
import com.predic8.schema.creator.*
import com.predic8.wstool.creator.*

class GroupTest extends GroovyTestCase{

  public static HR_NS = "http://predic8.com/human-resources/"
  public static PERSON_GROUP = new QName(HR_NS,"PersonGroup")
  public static EMPLOYEE_TYPE = new QName(HR_NS, "EmployeeType")
  def schema

  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse(input:"/group.xsd")
  }

  void testTopLevelGroup(){
    assertNotNull(schema.getGroup(PERSON_GROUP))
  }

  void testGroupInComplexType(){
    def group = schema.getType(EMPLOYEE_TYPE).model.particles.find{ it.name == 'person'}.embeddedType.model
    assertNotNull(group)
  }

  void testSchemaCreator(){
    def strWriter = new StringWriter()
    def creator = new SchemaCreator(builder : new MarkupBuilder(strWriter))
    schema.create(creator, new SchemaCreatorContext())
    def testSchema = new XmlSlurper().parseText(strWriter.toString())
    assertEquals(2, testSchema.group[0].sequence.element.size())
    assertEquals('tns:PersonGroup', testSchema.complexType[0].sequence.element.complexType.group.@ref.toString())
  }

  void testRequestCreator() {
    def strWriter = new StringWriter()
    def creator = new RequestCreator(builder : new MarkupBuilder(strWriter))
    schema.getElement('employee').create(creator, new RequestCreatorContext(formParams: ["xpath:/employee/person/firstName":"Kaveh", "xpath:/employee/person/lastName":"Keshavarzi"]))
    def emp = new XmlSlurper().parseText(strWriter.toString())
    assertEquals('Kaveh', emp.person.firstName.toString())
    assertEquals('Keshavarzi', emp.person.lastName.toString())
  }

  void testRequestTemplateCreator() {
    def strWriter = new StringWriter()
    def creator = new RequestTemplateCreator(builder : new MarkupBuilder(strWriter))
    schema.getElement('employee').create(creator, new RequestTemplateCreatorContext())
    def emp = new XmlSlurper().parseText(strWriter.toString())
    assertEquals('?XXX?', emp.person.firstName.toString())
    assertEquals('?XXX?', emp.person.lastName.toString())
  }
}