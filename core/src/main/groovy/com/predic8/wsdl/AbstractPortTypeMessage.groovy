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
import com.predic8.xml.util.*

abstract class AbstractPortTypeMessage extends WSDLElement {

	PrefixedName messagePrefixedName
	Message message

  protected parseAttributes(token, WSDLParserContext ctx){
    name = token.getAttributeValue(null , 'name')
    messagePrefixedName = new PrefixedName(token.getAttributeValue(null , 'message'))
  }
  
	Message getMessage() {
		if(message)	return message
		if(!messagePrefixedName) throw new ModelAccessException("No message declared in ${ELEMENTNAME.localPart}${name?' '+name:''}.", this)
		definitions.getMessage(getQNameForPN(messagePrefixedName))
	}
	
  void create(AbstractCreator creator, CreatorContext ctx) {
    creator.createPortTypeMessage(this, ctx)
  }
  
  Message newMessage(String name){
		message = definitions.newMessage(name)
  }	
  
}

