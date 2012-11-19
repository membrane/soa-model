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
import com.predic8.wsdl.soap11.Address as SOAP11Address;
import com.predic8.wsdl.soap12.Address as SOAP12Address;
import com.predic8.wsdl.http.Address as HttpAddress;
import com.predic8.wsdl.soap11.SOAPBinding as SOAP11Binding
import com.predic8.wsdl.soap12.SOAPBinding as SOAP12Binding
import groovy.xml.QName
import javax.xml.namespace.QName as JQName
import com.predic8.soamodel.Consts
//import com.sun.xml.internal.ws.fault.SOAP11Fault;

class Port extends WSDLElement{

  public static final JQName ELEMENTNAME = new JQName(Consts.WSDL11_NS, 'port')

  Binding binding
  AbstractAddress address

  protected parseAttributes(token, params){
    name = token.getAttributeValue(null , 'name')
    def pn = new PrefixedName(token.getAttributeValue(null , 'binding'))
    binding = definitions.getBinding(pn.localName)
  }

  protected parseChildren(token, child, params){
    super.parseChildren(token, child, params)
    switch (token.name ){
      case SOAP11Address.ELEMENTNAME:
      address = new SOAP11Address(definitions: definitions)
      address.parse(token, params) ; break
      case SOAP12Address.ELEMENTNAME:
      address = new SOAP12Address(definitions: definitions)
      address.parse(token, params) ; break
      case HttpAddress.ELEMENTNAME:
      address = new HttpAddress(definitions: definitions)
      address.parse(token, params) ; break
    }
  }
  
  def isSOAP11() {
    binding.binding instanceof SOAP11Binding
  }
  
  def create(creator, ctx) {
    creator.createPort(this, ctx)
  }
  
  public Binding newBinding(String name){
    binding = definitions.newBinding(name)
  }
  
  public SOAP11Address newSOAP11Address(String location){
    namespaces['soap'] = Consts.WSDL_SOAP11_NS
    address = new SOAP11Address(location: location , definitions: definitions, parent: this, namespaces : ['soap' : Consts.WSDL_SOAP11_NS])
  }
  
  public SOAP12Address newSOAP12Address(String location){
    namespaces['soap12'] = Consts.WSDL_SOAP12_NS
    address = new SOAP12Address(location: location, definitions: definitions, parent: this)
  }
  
  public HttpAddress HttpAddress(String location){
    namespaces['http'] = Consts.WSDL_HTTP_NS
    address = new HttpAddress(location: location, definitions: definitions, parent: this)
  }
  
  String toString() {
    "port[ name=$name, address=$address, binding=$binding ]"
  }
}