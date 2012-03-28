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

//import javax.xml.stream.*
import groovy.xml.*
import com.predic8.xml.util.*
import com.predic8.schema.creator.*
import com.predic8.wstool.creator.*

class CyclingElementsSchemaTest extends GroovyTestCase{

  public static HR_NS = "http://predic8.com/human-resources/"
  public static PERSON_GROUP = new QName(HR_NS,"PersonGroup")
  public static EMPLOYEE_TYPE = new QName(HR_NS, "EmployeeType")
  def schema

  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse(input:"/schema/cycling-elements.xsd")
  }

  void testRequestTemplateCreator() {
    def strWriter = new StringWriter()
    def creator = new RequestTemplateCreator(builder : new MarkupBuilder(strWriter))
    schema.getElement('area').create(creator, new RequestTemplateCreatorContext())
    def req = new XmlSlurper().parseText(strWriter.toString())
    assertEquals('?XXX?', req.country.toString())
    assertEquals('?XXX?', req.area.country.toString())
    assertEquals('?999?', req.area.population.toString())
    /* The recursive element is repeated 3 times and there are total 30 letters in the request. */
    assertEquals(30, req.toString().size())
  }
  
}
