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

import groovy.namespace.QName

import javax.xml.namespace.QName as JQName

import com.predic8.policy.*
import com.predic8.soamodel.*

class Service extends WSDLElement{

  public static final JQName ELEMENTNAME = new JQName(Consts.WSDL11_NS, 'service')
  List<Port> ports = []

  protected parseAttributes(token, WSDLParserContext ctx){
    name = token.getAttributeValue(null , 'name')
  }

  protected parseChildren(token, child, WSDLParserContext ctx){
    super.parseChildren(token, child, ctx)
    switch (token.name ){
      case Port.ELEMENTNAME :
      def port = new Port(definitions : definitions)
      port.parse(token, ctx)
      ports << port ; break
    }
  }

  void create(AbstractCreator creator, CreatorContext ctx) {
    creator.createService(this, ctx)
  }
  
  public Port newPort(String name){
    def port = new Port(definitions: definitions, name : name, parent: this)
    ports << port
    port
  }
  
  String toString() {
    "service[ ports=$ports ]"
  }

  JQName getElementName() {
    ELEMENTNAME
  }

}