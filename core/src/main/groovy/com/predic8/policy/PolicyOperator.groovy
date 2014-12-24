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

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.predic8.policy.creator.PolicyCreator
import com.predic8.soamodel.AbstractParserContext
import com.predic8.soamodel.Consts
import com.predic8.soamodel.CreatorContext
import com.predic8.soamodel.XMLElement
import com.predic8.wsdl.WSDLElement

abstract class PolicyOperator extends XMLElement {

	private static final Logger log = LoggerFactory.getLogger(PolicyOperator.class)

	static final String NAMESPACE = Consts.WSP15_NS
	
	/**
	 * ELEMENTNAME will be set at runtime. Depending on the used version,
	 * it should be the one from the XML document cause it will be used
	 * to find the end tag of the XML element.
	 */
	JQName ELEMENTNAME

	String name
	WSDLElement wsdlElement
	
	List<PolicyOperator> policyItems = []
	
	protected parseChildren(token, child, AbstractParserContext ctx){
		switch (token.name.namespaceURI){
			case {it in Consts.POLICY_NAMESPACES} :
				def pi = PolicyItemUtility.getPolicyItem(token.name)
				if(pi) {
					pi.parse(token, ctx) 
					policyItems << pi 
				}
				break
			default:
				ctx.errors << "Parsing ${token.name} not supported yet!"
				break
		}
	}

	def getAllPolicyItems() {
		(policyItems + policyItems*.allPolicyItems).flatten()
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
	
	void create(PolicyCreator creator, CreatorContext ctx){
		creator.createPolicyItem(this, ctx)
	}
	
}
