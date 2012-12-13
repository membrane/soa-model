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
import javax.xml.stream.*
import com.predic8.schema.creator.SchemaCreator
import com.predic8.schema.creator.SchemaCreatorContext
import com.predic8.soamodel.Consts;
import com.predic8.wstool.creator.*
import com.predic8.xml.util.*

class AttributeGroupTest extends GroovyTestCase{
  
  def schema
    
  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse(input:"/attributeGroup.xsd")
  }
    
  void testParser() {
    assertEquals('Attr1', schema.attributeGroups[0].attributes[0].name)
    assertEquals(new QName(Consts.SCHEMA_NS, 'string'), schema.getAttributeGroup('AttrG1').getAttribute('Attr2').type)
    assertEquals('AttrG1', schema.getType('EmployeeType').attributeGroups[0].ref.localPart)
    assertEquals(new QName('http://predic8.com/human-resources/', 'AttrG2'), schema.getAttributeGroup('AttrG1').attributeGroups[0].ref)
  }
  
  void testSchemaCreator() {
    def strWriter = new StringWriter()
    def creator = new SchemaCreator(builder : new MarkupBuilder(strWriter))
    schema.create(creator, new SchemaCreatorContext())
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse(input:new StringReader(strWriter.toString()))
    assertEquals('Attr1', schema.attributeGroups[0].attributes[0].name)
    assertEquals(new QName(Consts.SCHEMA_NS, 'string'), schema.getAttributeGroup('AttrG1').getAttribute('Attr2').type)
    assertEquals('AttrG1', schema.getType('EmployeeType').attributeGroups[0].ref.localPart)
    assertEquals(new QName('http://predic8.com/human-resources/', 'AttrG2'), schema.getAttributeGroup('AttrG1').attributeGroups[0].ref)
//    println strWriter
  }
  
  void testRequestTemplateCreater() {
    def strWriter = new StringWriter()
    def creator = new RequestTemplateCreator(builder : new MarkupBuilder(strWriter))
    schema.getElement('chef').create(creator, new RequestTemplateCreatorContext())
    def request = new XmlSlurper().parseText(strWriter.toString())
    assertEquals('?XXX?', request.@Attr1.toString())
    assertEquals('?999?', request.@Attr4.toString())
    assertEquals('?XXX?', request.@Attr5.toString())
    assertEquals('?999?', request.@Attr6.toString())
    assertEquals('?999?', request.@Attr7.toString())
    assertEquals('?XXX?', request.person.lastName.toString())
    assertEquals('?XXX?', request.person.@Attr3.toString())
    assertEquals('?999?', request.person.@Attr4.toString())
//    println strWriter
  }
  
  void testRequestCreater() {
    def strWriter = new StringWriter()
    def creator = new RequestCreator(builder : new MarkupBuilder(strWriter))
    def formParams = [:]
    formParams['xpath:/person/firstName']='Kaveh'
    //RequestCreator does not supported Attributes yet!
    //formParams['xpath:/person@Attr3']='Bla'
    schema.getElement('chef').create(creator, new RequestCreatorContext(formParams:formParams))
//    println strWriter
  }
}
