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

import com.predic8.soamodel.ElementRefAccessException
import com.predic8.soamodel.MessageAccessException
import com.predic8.soamodel.OperationAccessException
import com.predic8.soamodel.PortTypeAccessException
import com.predic8.soamodel.TypeRefAccessException;
import com.predic8.soamodel.ValidationError

class WSDLValidator {

	List<ValidationError> validate(Definitions wsdl, WSDLParserContext ctx) {
		if(ctx.validated.contains(wsdl)) return
		/* During validation of schemas, the list of localSchemas may be expanded by implicit schemas.
		 * So for the validator a clone should be created, which will not change in middle of the precess.
		 */
		def schemas = wsdl.localSchemas ? wsdl.localSchemas.clone() : []
		schemas*.validate(ctx)
		wsdl.services.each {
			validateServicePorts(it.ports, ctx)
		}
		validateBindings(wsdl.bindings, ctx)
		validatePortTypeOperations(wsdl, ctx)
		validatePortTypeMessages(wsdl.operations.input, ctx)
		validatePortTypeMessages(wsdl.operations.output, ctx)
		validatePortTypeMessages(wsdl.operations.faults.flatten(), ctx)
		wsdl.messages.each { msg ->
			if(! msg.parts) ctx.errors << new ValidationError(invalidElement : msg, parent: wsdl, message : "There is no part defined in message ${msg.name} in this WSDL.", wsdlTNS: wsdl.targetNamespace)
			else validateMessageParts(msg , ctx)
		}
		ctx.validated << wsdl
		return ctx.errors.grep(ValidationError)
	}

	void validateServicePorts(ports, ctx) {
		ports.each {
			try {
				if(!it.binding)
					ctx.errors << new ValidationError(invalidElement : it, message : "Port ${it.name} uses '${it.bindingPN}' as a binding reference, which is not defined in this WSDL.", wsdlTNS: it.definitions.targetNamespace)
			} catch (Exception e) {
				ctx.errors << new ValidationError(invalidElement : it, message : (e.message ?: "Port ${it.name} is invalid."), wsdlTNS: it.definitions.targetNamespace)
			}
		}
	}

	void validateBindings(bnds, ctx) {
		bnds.each { bnd ->
			try {
				if(!bnd.portType){
					def e = new PortTypeAccessException("Could not find the portType definition for '${bnd.typePN}' in the binding'${bnd.name}'.", bnd)
					ctx.errors << new ValidationError(invalidElement : bnd, message : "Binding ${bnd.name} uses '${bnd.typePN}' as a type reference which is not defined in this WSDL.",
					wsdlTNS: bnd.definitions.targetNamespace, cause: e)
				} else {
					bnd.operations.each{ bOp ->
						if(! (bOp.name in bnd.portType.operations.name)){
							def e = new OperationAccessException("Could not find the operation'${bOp.name}' in the portType '${bnd.typePN}'.", bOp)
							ctx.errors << new ValidationError(invalidElement : bOp, message : "Operation '${bOp.name}' not found in portType '${bnd.typePN}'.",
							wsdlTNS: bnd.definitions.targetNamespace, cause: e)
						}
					}
				}
			} catch (Exception e) {
				ctx.errors << new ValidationError(invalidElement : bnd, message : (e.message ?: "Binding ${bnd.name} is invalid."), wsdlTNS: bnd.definitions.targetNamespace)
			}
		}
	}

	void validatePortTypeOperations(Definitions wsdl, ctx) {
		wsdl.portTypes.each {pT ->
			wsdl.bindings.findAll{it.portType == pT}.each {bnd ->
				def missingOps = pT.operations.name - bnd.operations.name
				if(missingOps)
				ctx.errors << new ValidationError(invalidElement : bnd, wsdlTNS: bnd.definitions.targetNamespace,
					message : "Could not find the definition for binding operation: $missingOps in binding '${bnd.name}'.")
			}
		}
	}
	
	void validatePortTypeMessages(ptms, ctx) {
		ptms.each {
			def err
			try {
				if(it && !it.message){
					def e = new MessageAccessException("Could not find the message '${it.messagePrefixedName.toString()}', used in the ${it.ELEMENTNAME.localPart} of an operation.", it, it.messagePrefixedName.toString())
					err = new ValidationError(invalidElement : it, wsdlTNS: it.definitions.targetNamespace, cause: e,
					message : "Message '${it.messagePrefixedName}' in the ${it.ELEMENTNAME.localPart} ${it.name} is not defined in this WSDL.")
				}
			} catch (Exception e) {
				err = new ValidationError(invalidElement : it, message : (e.message ?: "The ${it.ELEMENTNAME.localPart} ${it.name} is invalid."), wsdlTNS: it.definitions.targetNamespace, cause: e)
			}
			if(err) {
				ctx.errors << err
			}
		}
	}

	void validateMessageParts(Message msg, ctx) {
		msg.parts.each {part ->
			try {
				part.element ?: part.type
			} catch (ElementRefAccessException e) {
				ctx.errors << new ValidationError(invalidElement : part, parent: msg,
					message : "The referenced element ${part.elementPN} in part ${part.name} of the message ${msg.name} is not defined in this WSDL.",
					wsdlTNS: part.definitions.targetNamespace, cause: e)
			} catch (TypeRefAccessException e) {
				ctx.errors << new ValidationError(invalidElement : part, parent: msg,
					message : "The referenced type ${part.typePN} in part ${part.name} of the message ${msg.name} is not defined in this WSDL.",
					wsdlTNS: part.definitions.targetNamespace, cause: e)
			} catch (Exception e) {
				ctx.errors << new ValidationError(invalidElement : part, parent: msg, 
					message : (e.message ?: "The part ${part.name} in message ${msg.name} is invalid."), wsdlTNS: part.definitions.targetNamespace)
			}
		}
	}
}
