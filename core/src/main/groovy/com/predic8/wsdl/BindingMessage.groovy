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

import groovy.xml.*
import groovy.namespace.*

import com.predic8.policy.Policy
import com.predic8.policy.PolicyReference
import com.predic8.soamodel.*
import com.predic8.wsdl.soap11.SOAPBody as SOAP11Body
import com.predic8.wsdl.soap11.SOAPFault as SOAP11Fault
import com.predic8.wsdl.soap11.SOAPHeader as SOAP11Header
import com.predic8.wsdl.soap12.SOAPBody as SOAP12Body
import com.predic8.wsdl.soap12.SOAPFault as SOAP12Fault
import com.predic8.wsdl.soap12.SOAPHeader as SOAP12Header

abstract class BindingMessage extends WSDLElement{
  
  BindingOperation bindingOperation
  List<BindingElement> bindingElements = []
	
	PolicyReference policyReference

  protected parseAttributes(token, WSDLParserContext ctx){
    name = token.getAttributeValue(null , 'name')
  }

  protected parseChildren(token, child, WSDLParserContext ctx){
    super.parseChildren(token, child, ctx)
    def be
    switch (token.name){
      case SOAP11Header.ELEMENTNAME :
      be = new SOAP11Header(definitions : definitions, parent : this); break
      case SOAP11Body.ELEMENTNAME :
      be = new SOAP11Body(definitions : definitions, parent : this); break
      case SOAP11Fault.ELEMENTNAME :
      be = new SOAP11Fault(definitions : definitions, parent : this); break
      case SOAP12Header.ELEMENTNAME :
    	be = new SOAP12Header(definitions : definitions, parent : this); break
      case SOAP12Body.ELEMENTNAME :
      be = new SOAP12Body(definitions : definitions, parent : this); break
      case SOAP12Fault.ELEMENTNAME :
      be = new SOAP12Fault(definitions : definitions, parent : this); break
			
			case {it.namespaceURI in Consts.POLICY_NAMESPACES && it.localPart == 'PolicyReference'}:
				policyReference = new PolicyReference(ELEMENTNAME: token.name)
				policyReference.parse(token, ctx) ; break
			
    }
    be?.parse(token, ctx)
    if(be) bindingElements << be
  }
  
  List<AbstractSOAPHeader> getSOAPHeaders(){
    bindingElements.findAll {it instanceof AbstractSOAPHeader}
  }
	
	Policy getPolicy() {
		definitions.policies[policyReference?.uri - '#']
	}
	
	List<String> getPolicyAssertions() {
		policy?.assertions
	}
  
  SOAP11Header newSOAP11Header(){
    BindingElement be = new SOAP11Header(definitions : definitions, parent : this)
    bindingElements << be
    be
  }

  SOAP11Body newSOAP11Body(List<Part> parts){
    def be = new SOAP11Body(definitions : definitions, parent : this, parts : parts)
    bindingElements << be
    be
  }
    
  SOAP11Body newSOAP11Body(){
    newSOAP11Body(message.parts)
  }
  
  SOAP11Fault newSOAP11Fault(List<Part> parts){
    def be = new SOAP11Fault(definitions : definitions, parent : this, parts: parts)
    bindingElements << be
    be
  }
  
  SOAP11Fault newSOAP11Fault(){
    newSOAP11Fault(message.parts)
  }
  
  SOAP12Body newSOAP12Body(List<Part> parts){
    def be = new SOAP12Body(definitions : definitions, parent : this, parts : parts)
    bindingElements << be
    be
  }
  
  SOAP12Body newSOAP12Body(){
    newSOAP12Body(message.parts)
  }
  
  protected Message getMessage(){
		Operation pTOperation
		try {
			PortType pT = bindingOperation.binding.portType
			if(!pT)	throw new PortTypeAccessException("Could not find the portType definition for '${bindingOperation.binding.typePN}' in the binding'${bindingOperation.binding.name}'.", bindingOperation.binding)
			pTOperation = pT.getOperation(bindingOperation.name)
			if(!pTOperation) throw new OperationAccessException("Could not find the matching operation for '${bindingOperation.name}' in the portType '${pT.name}'.", pT)
			if(!pTOperation."${getElementName().localPart}") throw new ModelAccessException("No ${getElementName().localPart} declared for operation ${pTOperation.name}.", pTOperation)
			definitions.getMessage(pTOperation."${getElementName().localPart}".message.qname)
		} catch(ModelAccessException e) {
			//OperationAccessException | PortTypeAccessException | ModelAccessException
			throw e
    } catch (Exception e) {
			def msgName = pTOperation."${getElementName().localPart}".messagePrefixedName.toString()
	    throw new MessageAccessException("Could not find the message '$msgName', used in the ${getElementName().localPart} of the operation '${bindingOperation.name}'.", pTOperation, msgName)
    }
  }
  
  void create(AbstractCreator creator, CreatorContext ctx) {
    creator.createBindingMessage(this, ctx)
  }
}
