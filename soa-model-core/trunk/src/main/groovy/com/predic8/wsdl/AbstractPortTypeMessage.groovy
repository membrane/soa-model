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

import groovy.xml.QName

import com.predic8.xml.util.*

abstract class AbstractPortTypeMessage extends WSDLElement {

  Message message

  protected parseAttributes(token, params){
    name = token.getAttributeValue(null , 'name')
    def messagePrefixedName = new PrefixedName(token.getAttributeValue(null , 'message'))
    if(definitions.getNamespace(messagePrefixedName.prefix)==definitions.targetNamespace){
      message = definitions.getMessage(messagePrefixedName.localName)
      return
    }
    message = definitions.getMessage(new QName(definitions.getNamespace(messagePrefixedName.prefix),messagePrefixedName.localName))
  }
  
  def create(creator, ctx) {
    creator.createPortTypeMessage(this, ctx)
  }
  
  Message newMessage(String name){
    message = definitions.newMessage(name)
  }  
  
}

