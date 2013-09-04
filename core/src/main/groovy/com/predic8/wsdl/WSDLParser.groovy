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

import javax.xml.stream.XMLStreamReader

import com.predic8.soamodel.*
import com.predic8.wsi.*

class WSDLParser extends AbstractParser{
	
	Registry registry

	Definitions parse(String input){
		super.parse(new WSDLParserContext(input: input))
	}
	
	Definitions parse(InputStream input){
		super.parse(new WSDLParserContext(input: input))
	}
	
	Definitions parse(WSDLParserContext ctx){
		super.parse(ctx)
	}
	
	protected Definitions parseLocal(XMLStreamReader token, WSDLParserContext ctx){
		def encoding = token.getCharacterEncodingScheme()
		if( !encoding || (encoding != 'UTF-8' && encoding != 'UTF-16')) ctx.wsiResults << new WSIResult(rule : 'R4003')
		def definitions
		while(token.hasNext()) {
			if (token.startElement) {
				if(token.name == Definitions.ELEMENTNAME) {
					definitions = new Definitions(baseDir : ctx.newBaseDir, resourceResolver: ctx.resourceResolver, registry : registry ?: new Registry())
					ctx.wsdlElementOrder << definitions
					definitions.parse(token, ctx)
				}
				else if(token.name.namespaceURI == Consts.WSDL20_NS) {
					throw new WSDLVersion2NotSupportedException("WSDL 2.0 is not supported yet.")
				}
				else {
					throw new WrongGrammerException("Expected root element '{http://schemas.xmlsoap.org/wsdl/}definitions' for the WSDL document but was '${token.name}'.", token.name, token.location)
				}
			}
			if(token.hasNext()) token.next()
		}
		if(!definitions) throw new RuntimeException("The parsed document ${ctx.input} is not a valid WSDL document.")
		definitions
	}
}