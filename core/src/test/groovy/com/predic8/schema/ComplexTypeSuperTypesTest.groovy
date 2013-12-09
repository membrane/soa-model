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

import groovy.xml.QName
import com.predic8.xml.util.*
import com.predic8.schema.diff.SchemaDiffGenerator 

class ComplexTypeSuperTypesTest extends GroovyTestCase {
  
  Schema schema
  
  void setUp(){
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse("/schema/ComplexTypeSuperType.xsd")
  }
  
	void testComplexTypeGetSuperTypes() {
		assert schema.getComplexType('cyclingType').superTypes[0].toString() == '{http://www.predic8.com/complextype/}cyclingType'
		assert schema.getType('loopingType').superTypes[0].toString() == '{http://www.predic8.com/complextype/}loopingType'
		assert !schema.getComplexType('PersonType').superTypes
		assert schema.getComplexType('EmployeeType').superTypes*.localPart == ['PersonType']
		assert schema.getComplexType('ChefType').superTypes*.localPart == ['EmployeeType','PersonType']
		assert schema.getComplexType('companyEmailType').superTypes*.toString() == ['{http://www.predic8.com/complextype/}emailType', '{http://www.w3.org/2001/XMLSchema}string']
	}
}

