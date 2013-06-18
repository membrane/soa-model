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

package com.predic8.xml.util

import groovy.xml.MarkupBuilder

import com.predic8.schema.SchemaParser
import com.predic8.schema.creator.SchemaCreator
import com.predic8.schema.creator.SchemaCreatorContext

class BOMFixTest extends GroovyTestCase {
  
  void testClasspathResolver() {
		def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    def schema = parser.parse("/aWithBom.xsd")
		def strWriter = new StringWriter()
		def creator = new SchemaCreator(builder : new MarkupBuilder(strWriter))
		schema.create(creator, new SchemaCreatorContext())
		assert strWriter.toString().startsWith("<xsd:schema targetNamespace='schemaA' attributeFormDefault='unqualified' elementFormDefault='qualified' xmlns:xsd='http://www.w3.org/2001/XMLSchema'>")
	}
}
