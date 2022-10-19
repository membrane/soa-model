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

import com.predic8.soamodel.*
import com.predic8.xml.util.*

class Operation extends WSDLElement{

  public static final JQName ELEMENTNAME = new JQName(Consts.WSDL11_NS, 'operation')
  Input input
  Output output
  List<Fault> faults = []

  Operation(){}
  
  Operation(String name, Definitions definitions){
    this.name = name
    this.definitions = definitions
  }
  
  protected parseAttributes(token, WSDLParserContext ctx){
    name = token.getAttributeValue( null , 'name')
  }

  protected parseChildren(token, child, WSDLParserContext ctx){
    super.parseChildren(token, child, ctx)
    switch (token.name){
      case Input.ELEMENTNAME :
      input = new Input(definitions : definitions)
      input.parse(token, ctx) ; break
      case Output.ELEMENTNAME:
      output = new Output(definitions: definitions)
      output.parse(token, ctx) ; break
      case Fault.ELEMENTNAME:
      def fault = new Fault(definitions: definitions)
      fault.parse(token, ctx)
      faults << fault ; break
    }
  }

  void create(AbstractCreator creator, CreatorContext ctx){
    creator.createOperation(this, ctx)
  }
  
  public Input newInput(String name){
    input = new Input(definitions:definitions, name:name)
  }
  
  public Output newOutput(String name){
    output = new Output(definitions:definitions, name:name)
  }
  
  String toString() {
    "operation[ name=$name, input=$input, output=$output, faults=$faults, documentation=$documentation ]"
  }

  JQName getElementName() {
    ELEMENTNAME
  }

}
