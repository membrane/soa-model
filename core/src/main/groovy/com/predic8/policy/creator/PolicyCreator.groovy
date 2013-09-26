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

package com.predic8.policy.creator

import com.predic8.policy.*
import com.predic8.soamodel.*
import com.predic8.wsdl.*

class PolicyCreator extends AbstractCreator {

	void createPolicy(Policy policy, CreatorContext ctx){
		def attrs = [:]
		if(policy.id) attrs["${policy.getPrefix(Consts.WSU_NS)}:Id"] = policy.id
		builder."${policy.prefix}:Policy"(attrs + getNamespaceAttributes(policy)) {
			policy.policyItems.each {
				it.create(this, ctx)
			}
		}
	}

	void createPolicyReference(PolicyReference policyRef, CreatorContext ctx){
		builder."${policyRef.prefix}:PolicyReference"(URI:policyRef.uri)
	}

	void createHeader(Header header, CreatorContext ctx){
		def attrs = [:]
		if(header.name) attrs['Name'] = header.name
		if(header.namespace) attrs['Namespace'] = header.namespace
		builder."${header.prefix}:Header"(attrs + getNamespaceAttributes(header)) {
			header.policyItems.each {
				it.create(this, ctx)
			}
		}
	}

	void createX509Token(X509Token token, CreatorContext ctx){
		def attrs = [:]
		if(token.includeToken) attrs["${token.prefix}:IncludeToken"] = token.includeToken
		builder."${token.prefix}:X509Token"(attrs + getNamespaceAttributes(token)) {
			token.policyItems.each {
				it.create(this, ctx)
			}
		}
	}
	
	void createUsernameToken(UsernameToken token, CreatorContext ctx){
		def attrs = [:]
		if(token.includeToken) attrs["${token.prefix}:IncludeToken"] = token.includeToken
		builder."${token.prefix}:UsernameToken"(attrs + getNamespaceAttributes(token)) {
			token.policyItems.each {
				it.create(this, ctx)
			}
		}
	}

	void createPolicyItem(PolicyOperator pi, CreatorContext ctx){
		builder."${pi.prefix}:${pi.ELEMENTNAME.localPart}"(getNamespaceAttributes(pi)) {
			pi.policyItems.each {
				it.create(this, ctx)
			}
		}
	}
}