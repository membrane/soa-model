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

class WSDLParser extends AbstractParser{
  
  Definitions parse(String input){
    WSDLParserContext ctx = new WSDLParserContext();
    ctx.setInput(input);
    parse(ctx)
  }
  
  Definitions parse(params){
    super.parse(params)
  }
  
  Definitions parseLocal(token, params){
    //    params.wsiResults = []
    def encoding = token.getCharacterEncodingScheme()
    if( !encoding || (encoding != 'UTF-8' && encoding != 'UTF-16')) params.wsiResults << new WSIResult(rule : 'R4003')
    def definitions
    while(token.hasNext()) {
      if (token.startElement) {
        def qname = token.name
        if(qname.getLocalPart() == 'definitions') {
          definitions = new Definitions(baseDir : params.newBaseDir, resourceResolver: params.resourceResolver)
          definitions.parse(token, , [wsiResults : params.wsiResults])
        }
      }
      if(token.hasNext()) token.next()
    }
    definitions
  }
}