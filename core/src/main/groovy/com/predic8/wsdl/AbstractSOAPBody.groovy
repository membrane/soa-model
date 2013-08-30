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

abstract class AbstractSOAPBody extends BindingElement {

  List<Part> parts = []
	
	protected List<String> partNames = []

  protected parseAttributes(token, params){
    super.parseAttributes(token, params)
    if(token.getAttributeValue(null , 'parts')){
      token.getAttributeValue(null , 'parts').split().each {
        partNames << it
      }
    }
  }
	
	/**
	 * lazy access to parts is needed cause of possible WSDL import structure.
	 */
	List<Part> getParts() {
		if(parts) return parts
		if(partNames) {
			parts = partNames.collect{getMessagePart(it)}
			return parts
		}
		parts = parent.message.parts
	}
  
  void create(AbstractCreator creator, CreatorContext ctx) {
    creator.createSOAPBody(this, ctx)
  }

  protected Part getMessagePart(String part){
  	parent.message.parts.find{it.name == part}
  }
}
