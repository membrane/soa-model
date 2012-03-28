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
import javax.xml.stream.*

class Documentation extends WSDLElement {
  
  public static final JQName ELEMENTNAME = new JQName(Consts.WSDL11_NS, 'documentation')
  
  String content = ''
  
  def parse(token, child){
    token.next()
    while(token.hasNext()) {
      if(token.startElement) {
        content += getStartTag(token)
      }
      if(token.getEventType() in [XMLStreamReader.CHARACTERS,XMLStreamReader.CDATA,XMLStreamReader.SPACE]) {
        content += token.getText()
      }
      if(token.endElement){
        if(token.name == ELEMENTNAME){
          break
        }
        content += (token.prefix)? "</${token.prefix}:${token.name.localPart}>" : "</${token.name.localPart}>"
      }
      if(token.hasNext()) token.next()
    }
  }
  
  private getStartTag(token){
    String str = ''
    str += (token.prefix)? "<${token.prefix}:${token.name.localPart} xmlns:${token.prefix}='${token.name.namespaceURI}'" : "<${token.name.localPart}"
    (token.attributeCount).times{ i ->
      def pre = (token.getAttributePrefix(i) != null)? " ${token.getAttributePrefix(i)}:" : ''
      str += " $pre${token.getAttributeName(i).localPart}='${token.getAttributeValue(i)}'"
    }
    str +=">"
  }
  
  def create(creator, ctx) {
    creator.createDocumentation(this, ctx.clone())
  }
  
  String toString() {
    content
  }
}
