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

package com.predic8.policy

import com.predic8.soamodel.*
import com.predic8.wsdl.Definitions;
import com.predic8.wsi.*

class PolicyParser extends AbstractParser{
	
	Policy parse(String input){
		super.parse(new PolicyParserContext(input: input))
	}
	
	Policy parse(PolicyParserContext ctx){
		super.parse(ctx)
	}

  def parseLocal(token, PolicyParserContext ctx){
		
//    def encoding = token.getCharacterEncodingScheme()
//    if( !encoding || (encoding != 'UTF-8' && encoding != 'UTF-16')) ctx.wsiResults << new WSIResult(rule : 'R4003')
    def policy
    while(token.hasNext()) {
      if (token.startElement) {
      	if(token.name.namespaceURI in Consts.POLICY_NAMESPACES && token.name.localPart == 'Policy') {
          policy = new Policy(ELEMENTNAME: token.name)
          policy.parse(token, , ctx)
        }
      }
      if(token.hasNext()) token.next()
    }
    policy
  }
}