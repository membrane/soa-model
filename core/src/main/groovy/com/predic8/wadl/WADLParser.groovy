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

package com.predic8.wadl

import javax.xml.stream.XMLStreamReader

import com.predic8.soamodel.AbstractParser
import com.predic8.soamodel.WrongGrammarException

class WADLParser extends AbstractParser{
	
	Application parse(String input){
		super.parse(new WADLParserContext(input: input))
	}
	
	Application parse(WADLParserContext ctx){
		super.parse(ctx)
	}
	
	protected Application parseLocal(XMLStreamReader token, WADLParserContext ctx){
		Application application
		while(token.hasNext()) {
			if (token.startElement) {
				if(token.name == Application.ELEMENTNAME) {
					application = new Application(baseDir : ctx.newBaseDir, resourceResolver: ctx.resourceResolver)
					application.parse(token, ctx)
				}
				else {
					throw new WrongGrammarException("Expected root element '{http://wadl.dev.java.net/2009/02}application' for the WADL document but was '${token.name}'.", token.name, token.location)
				}
			}
			if(token.hasNext()) token.next()
		}
		if(!application) throw new RuntimeException("The parsed document ${ctx.input} is not a valid WADL document.")
		application
	}
	
}