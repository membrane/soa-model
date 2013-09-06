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

import com.predic8.schema.creator.*
import com.predic8.wsdl.Definitions
import com.predic8.wsdl.WSDLParser
import com.predic8.wsdl.WSDLParserContext
import com.predic8.wstool.creator.*
import com.predic8.xml.util.*

class ImplicitSchemaTest extends GroovyTestCase{

	Definitions wsdl1 //Needs no implicit schemas
	Definitions wsdl2 //Needs one implicit schema
	Definitions wsdl3 //Needs two implicit schemas
	Definitions wsdl4 //Declares all schemas itself -> no need!
  
	protected void setUp() throws Exception {
		WSDLParser parser = new WSDLParser(resourceResolver: new ClasspathResolver())
		wsdl1 = parser.parse('BLZService.wsdl')
		wsdl2 = parser.parse('ImplicitSchema/BLZwithSOAPEnco.wsdl')
		wsdl3 = parser.parse('ImplicitSchema/BLZwithSOAPEncoAndXMLMime.wsdl')
	}
	
	void testSchemas() {
		def ctx = new WSDLParserContext()
		wsdl1.validate(ctx)
		wsdl2.validate(ctx)
		wsdl3.validate(ctx)
		/* wsdl2 & wsdl3 use types from soap-enc and xmlmime without importing the schemas.
		 * but there should not be an error in the context becaus of implicit schemas.
		 */
		assert 0 == ctx.errors.message.size()
	}
}
