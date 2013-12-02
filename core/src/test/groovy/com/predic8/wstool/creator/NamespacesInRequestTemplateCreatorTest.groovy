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

import javax.xml.stream.*

import com.predic8.schema.*
import com.predic8.wsdl.*
import com.predic8.xml.util.*

class NamespacesInRequestTemplateCreatorTest extends GroovyTestCase{
  
	Definitions wsdl
  
  void setUp() {
    def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
    wsdl = parser.parse("article/article.wsdl")
}
  
  void testRequestCreatorUnqualified() {
		def strWriter = new StringWriter()
    def creator = new RequestCreator(builder: new MarkupBuilder(strWriter))
		def formParams=["xpath:/create/article/name":'apple', "xpath:/create/article/price/amount":"123"]
		wsdl.getInputElementForOperation('ArticleServicePT', 'create').create(creator, new RequestCreatorContext(formParams:formParams))
		def request = strWriter.toString()
		assert request =~ /<ns1:create/
		assert request =~ /<article/
		assert request =~ /<ns2:name/
		assert request =~ /<ns2:description/
		assert request =~ /<ns2:price/
		assert request =~ /<ns3:amount/
		assert request =~ /<ns3:currency/
  }
	
  void testRequestCreatorQualified() {
		def strWriter = new StringWriter()
		def creator = new RequestCreator(builder: new MarkupBuilder(strWriter))
		def formParams=["xpath:/create/article/name":'apple', "xpath:/create/article/price/amount":"123"]
		wsdl.schemas*.elementFormDefault = "qualified"
		wsdl.getInputElementForOperation('ArticleServicePT', 'create').create(creator, new RequestCreatorContext(formParams:formParams))
		def request = strWriter.toString()
		assert request =~ /<ns1:create/
		assert request =~ /<ns1:article/
		assert request =~ /<ns2:name/
  }

  void testRequestTemplateCreatorUnqualified() {
    def strWriter = new StringWriter()
    def creator = new RequestTemplateCreator(builder: new MarkupBuilder(strWriter))
    wsdl.getInputElementForOperation('ArticleServicePT', 'create').create(creator, new RequestTemplateCreatorContext())
    def request = strWriter.toString()
    assert request =~ /<ns1:create/   
    assert request =~ /<article/
    assert request =~ /<ns2:name/
    assert request =~ /<ns2:description/
    assert request =~ /<ns2:price/
    assert request =~ /<ns3:amount/
    assert request =~ /<ns3:currency/
  }
  
  void testRequestTemplateCreatorQualified() {
  	def strWriter = new StringWriter()
  	def creator = new RequestTemplateCreator(builder: new MarkupBuilder(strWriter))
  	wsdl.schemas*.elementFormDefault = "qualified"
  	wsdl.getInputElementForOperation('ArticleServicePT', 'create').create(creator, new RequestTemplateCreatorContext())
  	def request = strWriter.toString()
  	assert request =~ /<ns1:create/   
  	assert request =~ /<ns1:article/
  	assert request =~ /<ns2:name/
  }
  
}
