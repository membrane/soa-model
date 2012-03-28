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

import com.predic8.xml.util.*
import groovy.xml.QName
import com.predic8.wsdl.soap11.SOAPBinding as SOAP11Binding
import com.predic8.wsdl.soap12.SOAPBinding as SOAP12Binding
import com.predic8.wsdl.http.HTTPBinding   as HTTPBinding
import javax.xml.namespace.QName as JQName
import org.apache.commons.logging.*
import com.predic8.soamodel.Consts
import com.predic8.policy.*

class Binding extends WSDLElement{

  private Log log = LogFactory.getLog(this.class)
  public static final JQName ELEMENTNAME = new JQName(Consts.WSDL11_NS, 'binding')
  
  PortType portType
  List<Operation> operations = []
  QName type
  AbstractBinding binding
  
  Binding(){
    // Default constructor  
  }
  
  public Binding(String name) {
    this.name = name
  }

  protected parseAttributes(token, params){
    name = token.getAttributeValue(null , 'name')
    type = getTypeQName(token.getAttributeValue(null , 'type'))
  }

  protected parseChildren(token, child, params){
    log.debug "found binding: ${token.name}"
    super.parseChildren(token, child, params)
    switch (token.name ){
      case Policy.ELEMENTNAME :
      def policy = new Policy()
      policy.parse(token, params) ; break
      case SOAP11Binding.ELEMENTNAME :
      log.debug "is soap11"
      binding = new SOAP11Binding(definitions: definitions)
      binding.parse(token, params) ;   break
      case SOAP12Binding.ELEMENTNAME :
      binding = new SOAP12Binding(definitions: definitions)
      binding.parse(token, params) ; break
      case Operation.ELEMENTNAME:
      def operation = new BindingOperation(definitions : definitions, binding: this)
      operation.parse(token, params)
      operations << operation ; break
      case HTTPBinding.ELEMENTNAME :
      binding = new HTTPBinding(definitions : definitions)
      binding.parse(token, params) ; break
    }
  }

  BindingOperation getOperation(String name){
    operations.find{it.name == name}
  }

  PortType getPortType(){
    definitions.getPortType(type)
  }

  def getProtocol() {
    binding.getProtocol()
  }
  
  SOAP11Binding newSOAP11Binding(){
    definitions.namespaces += ['soap':Consts.WSDL_SOAP11_NS]
    binding = new SOAP11Binding(definitions: definitions, parent: this)
  }
  
  SOAP11Binding newSOAP11Binding(String style){
    namespaces += ['soap':Consts.WSDL_SOAP11_NS]
    binding = new SOAP11Binding(definitions: definitions, style:style, parent: this)
  }
  
  SOAP11Binding newSOAP11Binding(String transport, String style){
    namespaces += ['soap':Consts.WSDL_SOAP11_NS]
    binding = new SOAP11Binding(definitions: definitions, transport: transport, style:style, parent: this)
  }
  
  SOAP12Binding newSOAP12Binding(){
    namespaces += ['soap12':Consts.WSDL_SOAP12_NS]
    binding = new SOAP12Binding(definitions: definitions, parent: this)
  }
  
  HTTPBinding newHTTPBinding(){
    namespaces += ['http':Consts.WSDL_HTTP_NS]
    binding = new HTTPBinding(definitions: definitions, parent: this)
  }
  
  BindingOperation newBindingOperation(String name){
    def op = new BindingOperation(name : name, definitions: definitions, binding: this, parent: this)
    operations << op
    op
  }
  
  void setType(PortType pt){
    type = new QName(definitions.targetNamespace, pt.name)
  }
  
  def create(creator, ctx) {
    creator.createBinding(this, ctx)
  }
  
  String toString() {
    "binding[name=$name, portType=$portType,type=$type,operations=$operations]"
  }
}