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

class MailingListRequestCreatorTest extends GroovyTestCase{

  def schema 

  public void setUp(){
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse(input:"/extern/schema1.xsd")
  }
  
  void testCreatRequestTemplate() {
    def strWriter = new StringWriter()
    def creator = new RequestTemplateCreator(builder : new MarkupBuilder(strWriter))
    def formParams = [:]
    schema.getElement('Request').create(creator, new RequestTemplateCreatorContext())
//	println strWriter
  }
  
  void testCreatRequest() {
    def strWriter = new StringWriter()
    def creator = new RequestCreator(builder : new MarkupBuilder(strWriter))
    def formParams = [:]
    formParams['xpath:/Request/MSISDN']='13723'
    formParams['xpath:/Request/ServiceList/Service/Id']='3223232'
    schema.getElement('Request').create(creator, new RequestCreatorContext(formParams:formParams))
//    def request = new XmlSlurper().parseText(strWriter.toString())
//    assertEquals('1', request.employee.id.text())
//    assertEquals('123', request.employee.department.id.text())
//    assertEquals('30', request.employee.@age.toString())
    println strWriter
  }
}
