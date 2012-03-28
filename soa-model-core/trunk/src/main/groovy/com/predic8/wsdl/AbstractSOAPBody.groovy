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

abstract class AbstractSOAPBody extends BindingElement {

  List<Part> parts = []

  protected parseAttributes(token, params){
    super.parseAttributes(token, params)
    if(!token.getAttributeValue(null , 'parts')){
      parts = message.parts
    } else {
      token.getAttributeValue(null , 'parts').split().each {
        parts << (getMessagePart(it))
      }
    }
  }

  protected Part getMessagePart(String part){
    message.parts.find{it.name == part}
  }

  protected Message getMessage(){
    definitions.getMessage(parent.bindingOperation.binding.portType.getOperation(parent.bindingOperation.name)."$parent.ELEMENTNAME.localPart".message.qname)
  }
  
  def create(creator, ctx) {
    creator.createSOAPBody(this, ctx)
  }

}
