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

import groovy.test.GroovyTestCase
import junit.framework.TestCase
import javax.xml.stream.*
import groovy.xml.*
import groovy.namespace.*

import com.predic8.schema.* 
import com.predic8.wstool.creator.*
import com.predic8.xml.util.*


class SchemaCreatorTest extends GroovyTestCase{
  
  def schema
  def schemaDefaultNamespace
    
  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse("/human-resources.xsd")
    schemaDefaultNamespace = parser.parse("/human-resources-default-namespace.xsd")
  }
    
  void testCreatorOutput() {
    def strWriter = new StringWriter()
    def creator = new SchemaCreator(builder : new MarkupBuilder(strWriter))
    schema.create(creator, new SchemaCreatorContext())
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse(new ByteArrayInputStream(strWriter.toString().bytes))
    assertNotNull(schema.getNamespace('ct'))
  }
  
  void testDefaultNamespace() {
    def strWriter = new StringWriter()
    def creator = new SchemaCreator(builder : new MarkupBuilder(strWriter))
    schemaDefaultNamespace.create(creator, new SchemaCreatorContext())    
    def testSchema = new XmlSlurper().parseText(strWriter.toString())
    assertEquals('xsd:string',testSchema.complexType[0].sequence.element[1].@type.toString()) 
  }
  
  void testMinMaxOccurs() {
    def strWriter = new StringWriter()
    def creator = new SchemaCreator(builder : new MarkupBuilder(strWriter))
    schema.create(creator, new SchemaCreatorContext())
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse(new ByteArrayInputStream(strWriter.toString().bytes))
    assertEquals('0', schema.getComplexType('EmployeeListType').getModel().getElement('employee').minOccurs)
    assertEquals('unbounded', schema.getComplexType('EmployeeListType').getModel().getElement('employee').maxOccurs)
//    println strWriter
  }
}
