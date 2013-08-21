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

import groovy.xml.*

import com.predic8.schema.*
import com.predic8.soamodel.Consts
import com.predic8.wsdl.WSDLParser
import com.predic8.xml.util.*

class NSConflictInRequestCreatorTest extends GroovyTestCase{

  def schema

  public void setUp(){
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse("prefix-conflict-test/Test4NamespaceConfilict.xsd")
  }
  
  void testCreatTemplateRequest() {
//		println schema.getElement('Test').requestTemplate
//		def requestTemplate = new XmlSlurper().parseText(schema.getElement('Test').requestTemplate)
//		assertEquals('programer', requestTemplate.employee.department.activity.text())
  }
  
  void testCreatRequestWithEmbeddedType() {
    def strWriter = new StringWriter()
    def creator = new RequestCreator(builder : new MarkupBuilder(strWriter))
    def formParams = [:]
    formParams['xpath:/Test/From1']='test'
    formParams['xpath:/Test/FromSecondSchema/e1']='ABC'
    formParams['xpath:/Test/FromSecondSchema/e2']='12345'
    formParams['xpath:/Test/FromSecondSchema/e3']='2013-08-19'
    schema.getElement('Test').create(creator, new RequestCreatorContext(formParams:formParams))
//		println strWriter
//    def request = new XmlSlurper().parseText(strWriter.toString())
//    assertEquals('1', request.employee[0].id.text())
//		assertEquals('predic8', request.employee[1].department.company.text())
  }
	
	void testSOARequest(){
		def wsdl = new WSDLParser(resourceResolver: new ClasspathResolver()).parse('prefix-conflict-test/NSConflictTest.wsdl')
		def sw = new StringWriter()
		def formParams = [:]
		formParams['xpath:/Test/FromFirstNS']='test'
		formParams['xpath:/Test/FromSecondNS/e1']='ABC'
		formParams['xpath:/Test/FromSecondNS/e2']='12345'
		formParams['xpath:/Test/FromSecondNS/e3']='2013-08-19'
		def creator = new SOARequestCreator(wsdl, new RequestCreator(),new MarkupBuilder(sw))
		creator.formParams = formParams
		creator.createRequest 'NSConflictTest', 'NewOperation', 'NSConflictTestSOAP'
//		println sw
	}
  
}
