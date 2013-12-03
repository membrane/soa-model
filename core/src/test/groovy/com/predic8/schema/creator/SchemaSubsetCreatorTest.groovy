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

package com.predic8.schema.creator

import groovy.xml.*

import javax.xml.stream.*

import com.predic8.schema.*
import com.predic8.wsdl.Definitions
import com.predic8.wsdl.WSDLParser
import com.predic8.wstool.creator.*
import com.predic8.xml.util.*


class SchemaSubsetCreatorTest extends GroovyTestCase{
  
	Definitions wsdl
    
  void setUp() {
    def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
    wsdl = parser.parse("hotel.wsdl")
  }

	void testSubsetSchema() {
		wsdl.messages.parts.flatten().element.each {
			assert 1 == new SchemaSubsetVisitor().createSchema4Element(it).elements.size()
		}
	}
	
	void testCyclingElement(){
		Schema schema = new Schema('http://predic8.com/tests/schemasubset/')
		ComplexType testType = schema.newComplexType('testType')
		Element test = schema.newElement('test')
		test.type = new QName('http://predic8.com/tests/schemasubset/','testType')
		Sequence seq = testType.newSequence()
		seq.particles << test
		assert new SchemaSubsetVisitor().createSchema4Element(test).asString	
	}
	
	void testParsedCyclingSchema(){
		Schema cyclingSchema = new SchemaParser(resourceResolver: new ClasspathResolver()).parse('schema/cycling-elements.xsd')
		assert new SchemaSubsetVisitor().createSchema4Element(cyclingSchema.getElement('area')).asString
	}
}
