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

import groovy.test.GroovyTestCase
import junit.framework.TestCase
import javax.xml.stream.*
import groovy.xml.*
import groovy.namespace.*

import com.predic8.xml.util.*
import com.predic8.schema.*
import com.predic8.schema.creator.*

class NumberRestrictionTest extends GroovyTestCase{
  
  def schema
  def createdSchema

  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse("/number-restriction.xsd")
  }
  
  void testRestrictions() {
    def strWriter = new StringWriter()
    def creator = new SchemaCreator(builder : new MarkupBuilder(strWriter))
    schema.create(creator, new SchemaCreatorContext())
    createdSchema = new XmlSlurper().parseText(strWriter.toString())
    schema.simpleTypes.each{ sT ->
      //assertEquals(sT.restriction.base.toString(), createdSchema.simpleType.find{it.@name == sT.name}.restriction.@base.toString())
      assertEquals("${sT.restriction.base.prefix}:${sT.restriction.base.localPart}", createdSchema.simpleType.find{it.@name == sT.name}.restriction.@base.toString())
    }
  }
  
}
