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
import com.predic8.soamodel.Consts
import com.predic8.policy.*

class PortType extends WSDLElement{

  public static final JQName ELEMENTNAME = new JQName(Consts.WSDL11_NS, 'portType')

  List<Operation> operations = []
  def ping = []
  
  protected parseAttributes(token, params){
    name = token.getAttributeValue( null , 'name')
  }

  protected parseChildren(token, child, params){
    super.parseChildren(token, child, params)
    switch (token.name ){
      case Policy.ELEMENTNAME :
      def policy = new Policy()
      policy.parse(token, params) ; break
      case Operation.ELEMENTNAME:
      def operation = new Operation(definitions : definitions)
      operation.parse(token, params)
      operations << operation ; break
    }
  }
  
  def create(creator, ctx){
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
    "portType[ operations=$operations, ping=$ping, documentation=$documentation ]"
  }
}