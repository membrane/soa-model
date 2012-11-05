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

package com.predic8.wsdl;

import groovy.xml.QName
import javax.xml.namespace.QName as JQName
import com.predic8.soamodel.Consts

class Message extends WSDLElement {


  public static final JQName ELEMENTNAME = new JQName(Consts.WSDL11_NS, 'message')

  List<Part> parts = []
  
  protected parseAttributes(token, params){
    name = token.getAttributeValue( null , 'name')
  }

  protected parseChildren(token, child, params){
    super.parseChildren(token, child, params)
    switch(token.name) {
      case Part.ELEMENTNAME :
      def part = new Part(definitions: definitions)
      part.parse(token, params)
      parts << part ; break
    }
  }
  
  QName getQname() {
    new QName(definitions.targetNamespace, name)
  }

  Part getPart(name){
    parts.find{it.name == name}
  }
  
  def create(creator, ctx){
    creator.createMessage(this, ctx)
  }
  
  Part newPart(String name, String element){
    Part part = new Part(name:name, definitions:definitions, element:element)
    parts << part
    part
  }
  
  String toString() {
    "message[qname:${getQname()}]"
  }
}