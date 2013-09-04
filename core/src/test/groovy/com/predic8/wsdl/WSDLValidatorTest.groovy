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

import groovy.xml.MarkupBuilder

import com.predic8.soamodel.ValidationError;
import com.predic8.wsdl.creator.WSDLCreator
import com.predic8.wsdl.creator.WSDLCreatorContext
import com.predic8.xml.util.ClasspathResolver

class WSDLValidatorTest extends GroovyTestCase{
  
  Definitions wsdl
  WSDLParserContext ctx = new WSDLParserContext()
  
	void setUp() {
		ctx.input = "article-with-validation-error/article.wsdl"
		def parser = new WSDLParser(resourceResolver: new ClasspathResolver(), )
		wsdl = parser.parse(ctx)
  }
	
	void testValidation() {
		wsdl.validate(ctx) 
//		ctx.errors.grep(ValidationError).invalidElement.each{
//			if(it instanceof Port){
//				def strWriter = new StringWriter()
//				new WSDLCreator(builder : new MarkupBuilder(strWriter)).createPort(it, new WSDLCreatorContext())
//				println strWriter
//			}
//		}
//		ctx.errors.grep(ValidationError).each { println it.message}
		assert 4 == ctx.errors.grep(ValidationError).size()
		assert ctx.errors.grep(ValidationError).invalidElement.grep(Binding)
		assert ctx.errors.grep(ValidationError).invalidElement.grep(Port)
		assert ctx.errors.grep(ValidationError).invalidElement.grep(Part)
		assert ctx.errors.grep(ValidationError).invalidElement.grep(Output)
		assert !ctx.errors.grep(ValidationError).invalidElement.grep(BindingOperation)
	}
  
}
