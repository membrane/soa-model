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

		
package com.predic8.policy

import javax.xml.namespace.QName as JQName

import org.apache.commons.logging.*

import com.predic8.schema.Element;
import com.predic8.soamodel.Consts
import com.predic8.soamodel.XMLElement
import com.predic8.wsdl.Definitions
import com.predic8.wsdl.WSDLElement;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Otherwise;
import com.sun.xml.internal.ws.org.objectweb.asm.Item;

abstract class PolicyOperator extends XMLElement {

	private Log log = LogFactory.getLog(this.class)

	static final String NAMESPACE = Consts.WSP15_NS
	
	String name
	WSDLElement wsdlElement
	All all
	ExactlyOne eOne
	
	//TODO List of policy items e.g. addressing, symetricbinding
	def policyItems = []
	
	protected parseChildren(token, child, ctx){
		switch (token.name){
			case {it == All.VERSION12 || it == All.VERSION15}:
				all = new All(ELEMENTNAME: token.name)
				all.parse(token, ctx) ; break
			case {it == ExactlyOne.VERSION12 || it == ExactlyOne.VERSION15}:
				eOne = new ExactlyOne(ELEMENTNAME: token.name)
				eOne.parse(token, ctx) ; break
			case {it == UsingAddressing.VERSION1 || it == UsingAddressing.VERSION2}:
				def adr = new UsingAddressing(ELEMENTNAME: token.name)
			policyItems << adr
			case {it == SymmetricBinding.VERSION1 || it == SymmetricBinding.VERSION2}:
				def sb = new SymmetricBinding(ELEMENTNAME: token.name)
				policyItems << sb
			default:
				ctx.errors << "Parsing ${token.name} not implemented yet!"
				break
		}
	}

	def getAllPolicyItems() {
		def result = []
		if(policyItems) result = policyItems
		if(all) result.addAll(all.allPolicyItems) 
		if(eOne) result.addAll(eOne.allPolicyItems) 
		result
	}
  
	String getNamespaceUri() {
		definitions.targetNamespace
	}
	
	/**
	 * Should return the prefix for the namespace of the element, like wsdl, soap, http & etc.
	 * Used in WSDLCreator.
	 */
	String getPrefix(){
		getPrefix(ELEMENTNAME.namespaceURI)
	}

  JQName getElementName() {
    ELEMENTNAME
  }
	
  def abstract create(creator , context)

}
