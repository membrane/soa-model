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

import com.predic8.soamodel.AbstractParserContext;
import com.predic8.soamodel.Consts
import com.predic8.soamodel.XMLElement
import com.predic8.wsdl.WSDLElement

abstract class PolicyOperator extends XMLElement {

	private Log log = LogFactory.getLog(this.class)

	static final String NAMESPACE = Consts.WSP15_NS
	
	String name
	WSDLElement wsdlElement
	
	List<PolicyOperator> policyItems = []
	
	def securityPolicies = []
	
	protected parseChildren(token, child, AbstractParserContext ctx){
		switch (token.name){
			case {it == Policy.VERSION12 || it == Policy.VERSION15 }:
				def pi = new Policy(ELEMENTNAME: token.name)
				pi.parse(token, ctx) 
				policyItems << pi ; break
			case {it == All.VERSION12 || it == All.VERSION15}:
				def pi = new All(ELEMENTNAME: token.name)
				pi.parse(token, ctx) 
				policyItems << pi ; break
			case {it == ExactlyOne.VERSION12 || it == ExactlyOne.VERSION15}:
				def pi= new ExactlyOne(ELEMENTNAME: token.name)
				pi.parse(token, ctx)
				policyItems << pi ; break
			case {it == UsingAddressing.VERSION1 || it == UsingAddressing.VERSION2}:
				def pi = new UsingAddressing(ELEMENTNAME: token.name)
				pi.parse(token, ctx)
				policyItems << pi ; break
			case {it == SymmetricBinding.VERSION1 || it == SymmetricBinding.VERSION2}:
				def pi = new SymmetricBinding(ELEMENTNAME: token.name)
				pi.parse(token, ctx)
				policyItems << pi ; break
			case {it == Header.VERSION1 || it == Header.VERSION2}:
				def pi = new Header(ELEMENTNAME: token.name)
				policyItems << pi ; break
			case {it == Body.VERSION1 || it == Body.VERSION2}:
				def pi = new Body(ELEMENTNAME: token.name)
				policyItems << pi ; break
			case {it == AlgorithmSuite.VERSION1 || it == AlgorithmSuite.VERSION2}:
				def pi = new AlgorithmSuite(ELEMENTNAME: token.name)
				policyItems << pi ; break
			case {it == UsernameToken.VERSION12 || it == UsernameToken.VERSION15}:
				def pi = new UsernameToken(ELEMENTNAME: token.name)
				policyItems << pi ; break
				
			//Other policy items:
			case {it.namespaceURI == Consts.SP_NS2 && it.localPart == 'ProtectionToken'}:
				securityPolicies << token.name ; break
			case {it.namespaceURI == Consts.SP_NS2 && it.localPart == 'X509Token'}:
				securityPolicies << token.name ; break
			case {it.namespaceURI == Consts.SP_NS2 && it.localPart == 'WssX509V3Token10'}:
				securityPolicies << token.name ; break
			case {it.namespaceURI == Consts.SP_NS2 && it.localPart == 'Layout'}:
				securityPolicies << token.name ; break
			case {it.namespaceURI == Consts.SP_NS2 && it.localPart == 'Strict'}:
				securityPolicies << token.name ; break
			case {it.namespaceURI == Consts.SP_NS2 && it.localPart == 'IncludeTimestamp'}:
				securityPolicies << token.name ; break
			case {it.namespaceURI == Consts.SP_NS2 && it.localPart == 'OnlySignEntireHeadersAndBody'}:
				securityPolicies << token.name ; break
			case {it.namespaceURI == Consts.SP_NS2 && it.localPart == 'Basic128'}:
				securityPolicies << token.name ; break
			case {it.namespaceURI == Consts.SP_NS2 && it.localPart == 'Wss11'}:
				securityPolicies << token.name ; break
			case {it.namespaceURI == Consts.SP_NS2 && it.localPart == 'MustSupportRefKeyIdentifier'}:
				securityPolicies << token.name ; break
			case {it.namespaceURI == Consts.SP_NS2 && it.localPart == 'MustSupportRefIssuerSerial'}:
				securityPolicies << token.name ; break
			case {it.namespaceURI == Consts.SP_NS2 && it.localPart == 'MustSupportRefThumbprint'}:
				securityPolicies << token.name ; break
			case {it.namespaceURI == Consts.SP_NS2 && it.localPart == 'MustSupportRefEncryptedKey'}:
				securityPolicies << token.name ; break
			case {it.namespaceURI == Consts.SP_NS2 && it.localPart == 'SignedSupportingTokens'}:
				securityPolicies << token.name ; break
			case {it.namespaceURI == Consts.SP_NS2 && it.localPart == 'WssUsernameToken10'}:
				securityPolicies << token.name ; break
			case {it.namespaceURI == Consts.SP_NS2 && it.localPart == 'EncryptedParts'}:
				securityPolicies << token.name ; break
			case {it.namespaceURI == Consts.SP_NS2 && it.localPart == 'SignedParts'}:
				securityPolicies << token.name ; break
				
			default:
				ctx.errors << "Parsing ${token.name} not implemented yet!"
				break
		}
	}

	def getAllPolicyItems() {
		(policyItems + policyItems*.allPolicyItems).flatten()
	}
	
	def getAllSecurityPolicies() {
		(securityPolicies + allPolicyItems*.allSecurityPolicies).flatten()
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
	
}
