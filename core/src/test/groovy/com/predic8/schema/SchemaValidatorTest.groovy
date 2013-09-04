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

import com.predic8.soamodel.ValidationError;
import com.predic8.wsdl.Definitions
import com.predic8.wsdl.WSDLParser
import com.predic8.wsdl.WSDLParserContext;
import com.predic8.xml.util.*

class SchemaValidatorTest extends GroovyTestCase {
  
  Definitions wsdl
	WSDLParserContext ctx = new WSDLParserContext()
  
  void setUp(){
		ctx.input = "schema-validation-test/ArticleService.wsdl"
    def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
    wsdl = parser.parse(ctx)
  }
  
	void testComplexTypeGetSuperTypes() {
		Schema schema1 = wsdl.getSchema('http://predic8.com/wsdl/material/ArticleService/1/')
//		assert ctx.errors.grep(ValidationError).size() == 8
		assert ctx.errors.grep(ValidationError).size() == 5
		assert ctx.errors[0].message == "ComplexType GrandGrandParent inherits from '{http://predic8.com/wsdl/material/ArticleService/1/}GrandX3Parent', which is not definded in this schema."
		assert ctx.errors[0].invalidElement instanceof ComplexType
//		assert ctx.errors[1].message == "Some element in this document uses '{http://predic8.com/common/1/}Street4Test' as its reference, which is not defined in this schema."
		assert ctx.errors[1].message == "Element getAll uses '{http://predic8.de/}GetAllType' as its type, which is not defined in this schema."
		assert ctx.errors[1].invalidElement instanceof Element
//		assert ctx.errors[2].message == "SimpleType IdentifierType inherits from '{http://predic8.com/common/1/}string', which is not definded in this schema."
//		assert ctx.errors[2].invalidElement instanceof SimpleType
		assert ctx.errors[2].message == "ComplexType GrandParent inherits from '{http://predic8.com/wsdl/material/ArticleService/1/}GrandX3Parent', which is not definded in this schema."
//		assert ctx.errors[3].message == "Element price uses '{http://predic8.com/common/1/}MoneyType' as its type, which is not defined in this schema."
		assert ctx.errors[3].message == "ComplexType Parent inherits from '{http://predic8.com/wsdl/material/ArticleService/1/}GrandX3Parent', which is not definded in this schema."
//		assert ctx.errors[4].message == "Element getAll uses '{http://predic8.de/}GetAllType' as its type, which is not defined in this schema."
		assert ctx.errors[4].message == "ComplexType Child inherits from '{http://predic8.com/wsdl/material/ArticleService/1/}GrandX3Parent', which is not definded in this schema."
	}
}

