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

package com.predic8.schema.restriction

import com.predic8.wstool.creator.FormCreatorContext;
import junit.framework.TestCase
import javax.xml.stream.*
import groovy.xml.*

import com.predic8.xml.util.*
import com.predic8.schema.*
import com.predic8.schema.creator.*
import com.predic8.wstool.creator.FormCreator;

class StringRestrictionTest extends GroovyTestCase{
  
  def schema
  def createdSchema

  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse(input:"/string-restriction.xsd")
  }
  
  void testSchemaCreator() {
    def strWriter = new StringWriter()
    def creator = new SchemaCreator(builder : new MarkupBuilder(strWriter))
    schema.create(creator, new SchemaCreatorContext())
    createdSchema = new XmlSlurper().parseText(strWriter.toString())
    assertEquals(schema.simpleTypes.size() , createdSchema.simpleType.size())
    assertEquals('xsd:string' , createdSchema.simpleType.find{it.@name == 'name'}.restriction.@base.toString())
  }
  
  void testFormCreatorWithStringRestriction(){
    def strWriter = new StringWriter()
    def creator = new FormCreator(builder : new MarkupBuilder(strWriter))
    schema.getElement('foo').form
    schema.getElement('foo').create(creator, new FormCreatorContext(formParams:'',path:"xpath:/"))
//    new File('C:/temp/minmax.html') << "<html><body><div>$strWriter</div></body></html>"
  }
  
//TODO: write a better test that make sense!
  void testFormCreatorWithEnumerationFacet(){
    def strWriter = new StringWriter()
    def creator = new FormCreator(builder : new MarkupBuilder(strWriter))
    schema.getElement('person').create(creator, new FormCreatorContext(formParams:'',path:"xpath:/"))
//    new File('C:/temp/enum.html') << "<html><body><div>$strWriter</div></body></html>"
  }
  
}
