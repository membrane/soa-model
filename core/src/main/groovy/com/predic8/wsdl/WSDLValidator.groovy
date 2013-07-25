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

package com.predic8.wsdl

class WSDLValidator {

	void validate(Definitions wsdl, WSDLParserContext ctx) {
		wsdl.services.each {
			validateServicePorts(it.ports, ctx)
		}
		validateBindings(wsdl.bindings, ctx)
		validatePortTypeMessages(wsdl.operations.input, ctx)
		validatePortTypeMessages(wsdl.operations.output, ctx)
		wsdl.messages.each { msg ->
			validateMessageParts(msg , ctx)
		}
	}
	
	void validateServicePorts(ports, ctx) {
		ports.each {
			if(!it.binding)
			ctx.errors << new ValidationError(invalidElement : it, message : "Port ${it.name} uses '${it.bindingPN}' as a binding reference, which is not defined in this WSDL.")
		}
	}
	
	void validateBindings(bnds, ctx) {
		bnds.each {
			if(!it.portType)
			ctx.errors << new ValidationError(invalidElement : it, message : "Binding ${it.name} uses '${it.typePN}' as a type reference which is not defined in this WSDL.")
		}
	}

	void validatePortTypeMessages(ptms, ctx) {
		ptms.each {
			def err
			try {
				if(!it.message){
					err = new ValidationError(invalidElement : it, message : "Message '${it.messagePrefixedName}' is not defined in this WSDL.")
				}
			} catch (Exception e) {
				err = new ValidationError(invalidElement : it, message : e.message)
			}
			if(err) ctx.errors << err
		}
	}

	void validateMessageParts(msg, ctx) {
		msg.parts.each {
			if(it.elementPN && !it.element) 
				ctx.errors << new ValidationError(invalidElement : it, message : "The referenced element ${it.elementPN} in part ${it.name} of the message ${msg.name} is not defined in this WSDL.")
			if(it.typePN && !it.type)
				ctx.errors << new ValidationError(invalidElement : it, message : "The referenced type ${it.typePN} in part ${it.name} of the message ${msg.name} is not defined in this WSDL.")
		}
	}

}
