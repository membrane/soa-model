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

import javax.xml.namespace.QName as JQName

import com.predic8.policy.*
import com.predic8.soamodel.*

class PortType extends WSDLElement{

  public static final JQName ELEMENTNAME = new JQName(Consts.WSDL11_NS, 'portType')

  List<Operation> operations = []
  def ping = []
  
  protected parseAttributes(token, ctx){
    name = token.getAttributeValue( null , 'name')
  }

  protected parseChildren(token, child, ctx){
    super.parseChildren(token, child, ctx)
    switch (token.name ){
      case Operation.ELEMENTNAME:
      def operation = new Operation(definitions : definitions)
      operation.parse(token, ctx)
      operations << operation ; break
    }
  }
  
  void create(AbstractCreator creator, CreatorContext ctx){
    creator.createPortType(this, ctx)
  }
  
  Operation getOperation(name) {
    operations.find { it.name == name}
  }
  
  Operation newOperation(String name){
    def op = new Operation(name, definitions)
    operations << op
    op
  }
  
  void addOperation(Operation op){
    op.parent = this
    operations << op
  }
  
  String toString() {
    "portType[name=$name,  operations=$operations, ping=$ping, documentation=$documentation ]"
  }
}