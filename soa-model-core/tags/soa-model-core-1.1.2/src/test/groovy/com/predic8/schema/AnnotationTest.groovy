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

import groovy.xml.*
import com.predic8.xml.util.*
import com.predic8.schema.diff.*
import com.predic8.schema.creator.*

class AnnotationTest extends GroovyTestCase {

  def schema1
  def schema2

  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema1 = parser.parse(input:"/annotation_1.xsd")
    schema2 = parser.parse(input:"/annotation_2.xsd")
  }

  void testParseAnnotation() {
    assertEquals(2, schema2.annotation.contents.size())
    assertEquals(2, schema2.getElement('employeeList').annotation.appinfos.size())
    assertEquals('de', schema2.getElement('employeeList').annotation.getDocumentation('ID2').lang)
    assertEquals('Note in CT', schema1.getType('EmployeeListType').annotation.appinfos[0].content)
    assertEquals('Documentation for ComplexType!', schema1.getType('EmployeeListType').annotation.documentations[0].content)
  }

  void testCompareSchema12() {
    def diffs = compare(schema1, schema2)
    assertNotNull(diffs.findAll{it.type == 'annotation'})
    assertEquals('annotation', diffs.findAll{it.type == 'complexType'}.diffs.flatten()[0].type)
    assertNotNull(diffs.findAll{it.type == 'complexType'}.diffs.flatten().find{it.type=='annotation'})
    assertEquals('annotation', diffs.findAll{it.type == 'element'}.diffs.flatten()[0].type)
    assertNotNull(diffs.findAll{it.type == 'element'}.diffs.flatten().find{it.type=='annotation'})
    def strWriter = new StringWriter()
    def creator = new SchemaCreator(builder : new MarkupBuilder(strWriter))
    schema2.create(creator, new SchemaCreatorContext())
  }

  private def compare(a, b) {
    new SchemaDiffGenerator(a: a, b: b).compare()
  }
	
}

