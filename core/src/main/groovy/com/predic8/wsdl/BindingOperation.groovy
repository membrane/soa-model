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
import com.predic8.wsdl.http.HTTPOperation
import com.predic8.wsdl.soap11.SOAPOperation as SOAP11Operation
import com.predic8.wsdl.soap12.SOAPOperation as SOAP12Operation


class BindingOperation extends WSDLElement{

  public static final JQName ELEMENTNAME = new JQName(Consts.WSDL11_NS, 'operation')
  ExtensibilityOperation operation
  BindingInput input
  BindingOutput output
  List<BindingFault> faults = []
  Binding binding

  protected parseAttributes(token, WSDLParserContext ctx){
    name = token.getAttributeValue(null , 'name')
  }

  protected parseChildren(token, child, WSDLParserContext ctx){
    super.parseChildren(token, child, ctx)
    switch (token.name) {
      case HTTPOperation.ELEMENTNAME :
      operation = new HTTPOperation(definitions : definitions)
      operation.parse(token, ctx) ; break
      case SOAP11Operation.ELEMENTNAME :
      operation = new SOAP11Operation(definitions : definitions)
      operation.parse(token, ctx) ; break
      case SOAP12Operation.ELEMENTNAME :
      operation = new SOAP12Operation(definitions : definitions)
      operation.parse(token, ctx) ; break
      case BindingInput.ELEMENTNAME :
      input = new BindingInput(definitions : definitions, bindingOperation : this)
      input.parse(token, ctx) ; break
      case BindingOutput.ELEMENTNAME :
      output = new BindingOutput(definitions : definitions, bindingOperation : this)
      output.parse(token, ctx) ; break
      case BindingFault.ELEMENTNAME :
      def fault = new BindingFault(definitions : definitions, bindingOperation : this)
      fault.parse(token, ctx)
      faults << fault ; break
    }
  }
	
  void create(AbstractCreator creator, CreatorContext ctx) {
    creator.createBindingOperation(this, ctx)
  }
  
  SOAP11Operation newSOAP11Operation(){
    operation = new SOAP11Operation(definitions:definitions, namespaces : ['soap':Consts.WSDL_SOAP11_NS], parent: this)
  }
  
  SOAP12Operation newSOAP12Operation(){
    operation = new SOAP12Operation(definitions:definitions, namespaces : ['soap12':Consts.WSDL_SOAP12_NS], parent: this)
  }
  
  HTTPOperation newHTTPOperation(){
    operation = new HTTPOperation(definitions:definitions, namespaces : ['http':Consts.WSDL_HTTP_NS], parent: this)
  }
  
  BindingInput newInput(){
    input = new BindingInput(definitions:definitions, bindingOperation : this, parent: this)
  }
  
  BindingOutput newOutput(){
    output = new BindingOutput(definitions:definitions, bindingOperation : this, parent: this)
  }
  
  
  String toString() {
    "bindingOperation[name=$name, operation=$operation, inputUse=${input?.bindingElements?.use.unique()},outputUse=${output?.bindingElements?.use}]"
  }

  JQName getElementName() {
    ELEMENTNAME
  }

}