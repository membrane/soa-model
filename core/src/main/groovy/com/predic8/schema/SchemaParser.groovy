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

import javax.xml.stream.XMLStreamReader

import com.predic8.soamodel.AbstractParser
import com.predic8.soamodel.WrongGrammarException

class SchemaParser extends AbstractParser{
  
	Schema parse(String input){
		super.parse(new SchemaParserContext(input: input))
	}
	
	Schema parse(InputStream input){
		super.parse(new SchemaParserContext(input: input))
	}
	
	Schema parse(SchemaParserContext input){
		super.parse(input)
	}
  
  def parseLocal(XMLStreamReader token, ctx){
    def schema
    while(token.hasNext()){
      if (token.startElement) {
        if(token.name == Schema.ELEMENTNAME) {
          def schemaLocation = ctx.input.hasProperty('schemaLocation') ? ctx.input.schemaLocation : ctx.input
          schema = new Schema(baseDir : ctx.newBaseDir ?: '', schemaLocation: schemaLocation ?: '', resourceResolver: ctx.resourceResolver)
          schema.parse(token, ctx)
        }
				else {
					throw new WrongGrammarException("Expected root element '{http://www.w3.org/2001/XMLSchema}schema' for the schema document but was '${token.name}'.", token.name, token.location)
				}
      }
      if(token.hasNext()) token.next()
    }

    if(!schema) throw new RuntimeException("The parsed document ${ctx.input} is not a valid schema document.")
    schema
  }
  
}
