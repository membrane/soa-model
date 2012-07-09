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

package com.predic8.schema.namespace

import junit.framework.TestCase
import javax.xml.stream.*
import groovy.xml.*

import com.predic8.schema.* 
import com.predic8.schema.creator.SchemaCreator;
import com.predic8.schema.creator.SchemaCreatorContext;
import com.predic8.wstool.creator.*
import com.predic8.xml.util.*

class SchemaWithXMLPrefixTest extends GroovyTestCase{
  
  def schema
    
  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse(input:"/namespaces.xsd")
  }
    
  void testCreatorOutput() {
    def strWriter = new StringWriter()
    def creator = new SchemaCreator(builder : new MarkupBuilder(strWriter))
    schema.create(creator, new SchemaCreatorContext())
    assertEquals('base', schema.attributeGroups[0].attributes[0].ref.localPart)
    assertEquals('http://www.w3.org/XML/1998/namespace', schema.attributeGroups[0].attributes[0].ref.namespaceURI)
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse(input:new StringReader(strWriter.toString()))
//    println strWriter
  }
}
