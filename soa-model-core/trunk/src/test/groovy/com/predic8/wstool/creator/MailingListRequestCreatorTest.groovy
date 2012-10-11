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
//    def strWriter = new StringWriter()
//    def creator = new RequestTemplateCreator(builder : new MarkupBuilder(strWriter))
//    def formParams = [:]
//    schema.getElement('Request').create(creator, new RequestTemplateCreatorContext())
//	println strWriter
  }
  
//  void testCreatRequest() {
//    def strWriter = new StringWriter()
//    def creator = new RequestCreator(builder : new MarkupBuilder(strWriter))
//    def formParams = [:]
////    formParams['xpath:/Request/Service']='3'
//    formParams['xpath:/Request/MSISDN']='sala'
//    formParams['xpath:/Request/ServiceList/Service[1]/accountId']='3223232'
//    formParams['xpath:/Request/ServiceList/Service[1]/assetStatus[1]']='Suspended'
//    formParams['xpath:/Request/ServiceList/Service[1]/assetStatus[2]']='Active'
//    formParams['xpath:/Request/ServiceList/Service[2]/accountId']='2'
//    formParams['xpath:/Request/ServiceList/Service[2]/assetStatus[1]']='foo'
//    formParams['xpath:/Request/ServiceList/Service[2]/assetStatus[2]']='bar'
//    schema.getElement('Request').create(creator, new RequestCreatorContext(formParams:formParams))
//    println strWriter
//  }
}
