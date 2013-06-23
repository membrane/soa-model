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

import com.predic8.soamodel.*

import com.predic8.wsi.*
//import com.predic8.soamodel.C

class WSDLParser extends AbstractParser{

	Definitions parse(String input){
		super.parse(new WSDLParserContext(input: input))
	}
	
	Definitions parse(InputStream input){
		super.parse(new WSDLParserContext(input: input))
	}
	
	Definitions parse(Reader input){
		super.parse(new WSDLParserContext(input: input))
	}
	
	Definitions parse(WSDLParserContext input){
		super.parse(input)
	}
	
	protected Definitions parseLocal(token, ctx){
		def encoding = token.getCharacterEncodingScheme()
		if( !encoding || (encoding != 'UTF-8' && encoding != 'UTF-16')) ctx.wsiResults << new WSIResult(rule : 'R4003')
		def definitions
		while(token.hasNext()) {
			if (token.startElement) {
				if(token.name == Consts.WSDL11_DEFINITIONS) {
					definitions = new Definitions(baseDir : ctx.newBaseDir, resourceResolver: ctx.resourceResolver)
					definitions.parse(token, ctx)
				}
				else if(token.name == Consts.WSDL20_DEFINITIONS) {
					throw new RuntimeException("WSDL 2.0 is not supported yet.")
				}
			}
			if(token.hasNext()) token.next()
		}
		if(!definitions) throw new RuntimeException("The parsed document ${ctx.input} is not a valid WSDL document.")
		definitions
	}
}