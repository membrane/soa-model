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

package com.predic8.wsdl

import com.predic8.soamodel.ElementRefAccessException;
import com.predic8.soamodel.MessageAccessException
import com.predic8.soamodel.NamespaceNotDeclaredForReferenceException
import com.predic8.soamodel.TypeRefAccessException;
import com.predic8.soamodel.ValidationError
import com.predic8.xml.util.ClasspathResolver

class WSDLValidatorTest extends GroovyTestCase{
  
  Definitions wsdl
  WSDLParserContext ctx
  
	void setUp() {
		ctx = new WSDLParserContext()
		ctx.input = "article-with-validation-error/article.wsdl"
		def parser = new WSDLParser(resourceResolver: new ClasspathResolver(), )
		wsdl = parser.parse(ctx)
  }
	
	void testValidation() {
		wsdl.validate(ctx)
		assert ctx.errors.grep(ValidationError).find{it.invalidElement instanceof Binding}.cause.message ==
		"Could not find the portType definition for 'tns:ArticleServicePTTEST' in the binding'ArticleServicePTBinding'."
		assert 8 == ctx.errors.grep(ValidationError).size()
		assert ctx.errors.grep(ValidationError).invalidElement.grep(Binding)
		assert ctx.errors.grep(ValidationError).invalidElement.grep(Port)
		assert ctx.errors.grep(ValidationError).invalidElement.grep(Part)
		assert ctx.errors.grep(ValidationError).invalidElement.grep(Output)
		assert ctx.errors.grep(ValidationError).find{it.invalidElement instanceof Output}.cause instanceof NamespaceNotDeclaredForReferenceException
		assert ctx.errors.grep(ValidationError).find{it.invalidElement instanceof Output}.cause.message ==
		"No namespace declared for prefix 'TEST', used to reference 'TEST:getAllResponse1' in output 'output3'."
		assert ctx.errors.grep(ValidationError).find{it.invalidElement instanceof Input}.cause instanceof MessageAccessException
		assert ctx.errors.grep(ValidationError).find{it.invalidElement instanceof Input}.cause.message ==
		"Could not find the message 'tns:getRequestTest', used in the input of an operation."
		assert ctx.errors.grep(ValidationError).find{it.invalidElement instanceof Fault}.message ==
		"Message 'tns:testFaultMessage' in the fault TestFault is not defined in this WSDL."
		assert !ctx.errors.grep(ValidationError).invalidElement.grep(BindingOperation)
	}
	
	void testValidatingBindingOperation() {
		wsdl.portTypes[0].name = 'ArticleServicePTTEST'
		wsdl.validate(ctx)
		assert ctx.errors.grep(ValidationError).findAll{it.invalidElement instanceof BindingOperation}.size() == 2
		ctx.errors.grep(ValidationError).findAll{it.invalidElement instanceof BindingOperation}[0].message == 
		"Operation 'test' not found in portType 'tns:ArticleServicePTTEST'."
		ctx.errors.grep(ValidationError).findAll{it.invalidElement instanceof BindingOperation}[1].message == 
		"Operation 'salam_Kaveh' not found in portType 'tns:ArticleServicePTTEST'."
	}
  
	void testBindingMissingOperation(){
		wsdl.validate(ctx)
		assert ctx.errors.grep(ValidationError).findAll{it.invalidElement.name == 'BindingMissingOperation'}[0].message ==
		"Could not find the definition for binding operation: [create, get, getAll] in binding 'BindingMissingOperation'."
	}
	
	void testPartValidation() {
		wsdl.validate(ctx)
		assert ctx.errors.grep(ValidationError).findAll{it.invalidElement instanceof Part}[0].message ==
		"The referenced element create in part parameters of the message createRequest is not defined in this WSDL."
		assert ctx.errors.grep(ValidationError).findAll{it.invalidElement instanceof Part}[0].cause instanceof ElementRefAccessException 
		assert ctx.errors.grep(ValidationError).findAll{it.invalidElement instanceof Part}[0].cause.message ==
		"Could not find the referenced element 'create' in namespace 'http://schemas.xmlsoap.org/wsdl/'."
		
		assert ctx.errors.grep(ValidationError).findAll{it.invalidElement instanceof Part}[1].message ==
		"The referenced type tns:CreateTypeTEST in part parameters of the message getAllRequestTEST is not defined in this WSDL."
		assert ctx.errors.grep(ValidationError).findAll{it.invalidElement instanceof Part}[1].cause instanceof TypeRefAccessException 
		assert ctx.errors.grep(ValidationError).findAll{it.invalidElement instanceof Part}[1].cause.message ==
		"Could not find the referenced type 'CreateTypeTEST' in namespace 'http://predic8.com/wsdl/material/ArticleService/1/'."
	}
}
