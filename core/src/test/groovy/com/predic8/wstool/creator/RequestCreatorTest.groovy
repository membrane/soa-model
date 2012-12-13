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

import com.predic8.schema.*
import com.predic8.xml.util.*
import groovy.xml.*

class RequestCreatorTest extends GroovyTestCase{

  def schema 

  public void setUp(){
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse(input:"/schema/complexcontent/person-a.xsd")
  }
  
  void testCreatTemplateRequest() {
    def strWriter = new StringWriter()
    def creator = new RequestTemplateCreator(builder : new MarkupBuilder(strWriter))
    def formParams = [:]
    schema.getElement('employeeList').create(creator, new RequestTemplateCreatorContext())
  }
  
  void testCreatRequestWithEmbeddedType() {
    def strWriter = new StringWriter()
    def creator = new RequestCreator(builder : new MarkupBuilder(strWriter))
    def formParams = [:]
    formParams['xpath:/employeeList/employee/firstName']='Kaveh'
    formParams['xpath:/employeeList/employee/lastName']='Keshavarzi'
    formParams['xpath:/employeeList/employee/id']='1'
    formParams['xpath:/employeeList/employee/department/id']='123'
    formParams['xpath:/employeeList/employee/@age']='30'
    formParams['xpath:/employeeList/employee[1]/firstName']='Shaan'
    formParams['xpath:/employeeList/employee[1]/lastName']='J'
    formParams['xpath:/employeeList/employee[2]/firstName']='Malkhas'
    schema.getElement('employeeList').create(creator, new RequestCreatorContext(formParams:formParams))
    def request = new XmlSlurper().parseText(strWriter.toString())
    assertEquals('1', request.employee.id.text())
    assertEquals('123', request.employee.department.id.text())
    assertEquals('30', request.employee.@age.toString())
    
  }
  
  void testCreatRequestWithRefType() {
    def strWriter = new StringWriter()
    def creator = new RequestCreator(builder : new MarkupBuilder(strWriter))
    def formParams = [:]
    formParams['xpath:/employee/firstName']='Shaan'
    formParams['xpath:/employee/lastName']='J'
    formParams['xpath:/employee@age']='29'
    schema.getElement('employee').create(creator, new RequestCreatorContext(formParams:formParams))
  }
  
  void testGetElementXpaths(){
    def strWriter = new StringWriter()
    def creator = new RequestCreator(builder : new MarkupBuilder(strWriter))
    def formParams = [:]
    formParams['xpath:/employeeList/employee/firstName']='Kaveh'
    formParams['xpath:/employeeList/employee[1]/firstName']='Shaan'
    formParams['xpath:/employeeList/employee[1]/lastName']='J'
    formParams['xpath:/employeeList/employee[2]/firstName']='Malkhas'
    formParams['xpath:/employeeList/employee[2]/abc/def/']='Tobias'
    def ctx = new RequestCreatorContext(formParams:formParams)
    ctx.path = "xpath:/employeeList/"
    ctx.element = schema.getElement('employee')
    assertEquals(['xpath:/employeeList/employee/', 'xpath:/employeeList/employee[1]/', 'xpath:/employeeList/employee[2]/'], creator.getElementXpaths(ctx))
  }
}
