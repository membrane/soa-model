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

import com.predic8.schema.*
import com.predic8.soamodel.*
import com.predic8.wsdl.*

class PolicyVisitor extends AbstractCreator {

	public static PolicyVisitorContext getWSDLPolicyInfo(Definitions wsdl, PolicyVisitorContext ctx = new PolicyVisitorContext()) {
		wsdl.bindings.each {
			it.create(this, ctx)
		}
//		wsdl.operations.each {
//			//TODO
//		}
	}

	public void createBinding(Binding binding, PolicyVisitorContext ctx){
		if(binding.policyReference) ctx.policyRefs[binding] = binding.policy
		binding.operations.each {
			it.create(this, ctx)
		}
	}

	public void createBindingOperation(BindingOperation operation, PolicyVisitorContext ctx){
		operation.input.create(this, ctx)
		operation.output.create(this, ctx)
		operation.faults.each {
			it.create(this, ctx)
		}
	}

	public void createBindingMessage(BindingMessage bndmsg, PolicyVisitorContext ctx){
		if(bndmsg.policyReference) ctx.policyRefs[bndmsg] = bndmsg.policy
	}

}