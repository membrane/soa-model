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

package com.predic8.wsdl.style

import com.predic8.wsdl.Binding

class RPCStyle extends BindingStyle {

	String value = 'rpc'

	Map check(Binding binding) {
		def operations = binding.operations
		operations.operation.style.each {
			if(it && it != value) {
				value = 'mixed'
				return
			}
		}
		def value = value.capitalize()
		def usages = (([operations.input]+ [operations.output]).bindingElements.use).flatten().unique()
		def err = [:]
		if(usages.size() > 1) {
			//Error! More than one use in operation
			err['message'] = "The value of the 'use' attribute is mixed."
			err['type'] = "MixedUseInOperation"
			return [result : "$value/Mixed", errors : [err]]
		}
		
		if(usages[0] == 'encoded') {
			return [result : "$value/Encoded"]
		}
		
		if(usages[0] == 'literal') {
			if(value == 'Mixed') {
				err['message'] = "The style in binding ${binding.name} has mixed values."
				err['type'] = "MixedStyle"
				return [result : "$value/Literal", errors : [err]]
			}
			return [result : "$value/Literal", errors : []]
		}
		
		err['message'] = "Could not detect the 'use' for the operations of binding ${binding.name}"
		err['type'] = "UnknownUse"
		return [result : 'Unknown!', errors : [err]]
	}
	
}
