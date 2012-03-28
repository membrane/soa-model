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

import com.predic8.wsdl.*
import com.predic8.xml.util.*

class IncludeTest extends GroovyTestCase {
  
  def schema
  def schema2
  
  void setUp() {
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse(input:"/include.xsd")
   
    parser = new SchemaParser(resourceResolver: new ClasspathResolver())    
    schema2 = parser.parse(input:"/common.xsd")
  }
    
  void testNotIncludedElement(){
    assertNotNull(schema.getElement('city'))
  }
  void testIncludedType(){
    assertNotNull(schema.getType('IdentifierType'))
  }
  
  void testTargetNamespace() {
    assertNotNull(schema2.asXml([:]))
  }
  
  void testBuildInTypeName() {
    assertEquals('string', schema2.getType('PersonType').model.getElement('id').buildInTypeName)
    assertEquals('string', schema2.getType('PersonType').model.getElement('firstName').buildInTypeName)
    assertEquals(null, schema2.getType('PersonType').model.getElement('address').buildInTypeName)
  }
  
}
