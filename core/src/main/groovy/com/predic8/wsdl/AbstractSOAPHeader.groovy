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

import javax.xml.namespace.QName as JQName
import com.predic8.soamodel.Consts
import com.predic8.xml.util.*

abstract class AbstractSOAPHeader extends BindingElement{

  Message message
  String part

  protected parseAttributes(token, params){
    super.parseAttributes(token, params)
    message = definitions.getMessage(new PrefixedName(token.getAttributeValue(null , 'message')).localName)
    part = token.getAttributeValue(null , 'part')
  }

  protected getMessagePart(){
    message.parts.find{it.name == part}
  }
  
  def create(creator, ctx) {
    creator.createSOAPHeader(this, ctx)
  }
}
