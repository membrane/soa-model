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

import com.predic8.schema.creator.*
import groovy.test.GroovyTestCase
import junit.framework.TestCase
import javax.xml.stream.*
import com.predic8.xml.util.*

import groovy.xml.*
import groovy.namespace.*
class SchemaFragmentDeclNSTest extends GroovyTestCase{
  
  def schema

  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse("/qualified-locals.xsd")
  }
  
  void testWithdeclNS() {
    def element = new XmlSlurper().parseText(schema.getElement('address').getSchemaFragment(new SchemaCreatorContext()))
    assertEquals('http://www.w3.org/2001/XMLSchema',element.lookupNamespace('xsd'))
    assertEquals('qualified',element.complexType.sequence.element[1].lookupNamespace('tns'))
  }  

}
