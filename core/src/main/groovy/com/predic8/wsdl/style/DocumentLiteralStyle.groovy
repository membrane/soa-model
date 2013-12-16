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

import com.predic8.soamodel.ModelAccessException
import com.predic8.soamodel.PortTypeAccessException
import com.predic8.wsdl.*

class DocumentLiteralStyle extends BindingStyle {

	String value = 'document'
	String doclit = "Document/Literal"

	
	//TODO Gemeinsame Teile der Methode mit RPCStyle in die Super-Klasse verlagern
	Map check(Binding binding) {
//		super.check(binding)
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
			err['message'] = "The value of the 'use' attribute should be 'literal' in all binding elements."
			err['type'] = "DocumentEncoded"
			return [result : "$value/Encoded", errors : [err]]
		}
		if(value == 'Mixed' && usages[0] == 'literal') {
			err['message'] = "The style in binding ${binding.name} has mixed values."
			err['type'] = "MixedStyle"
			return [result : "$value/Literal", errors : [err]]
		}
		if(value == 'Document' && usages[0] == 'literal') {
			if(!binding.portType) throw new PortTypeAccessException("Could not find the portType '${binding.typePN}' referenced in binding '${binding.name}'.", binding)
			return [result : checkWrapped(operations, binding.portType), errors : checkDocLitErrors(operations, binding.portType)]
		}
		err['message'] = "Could not detect the 'use' for the operations of binding ${binding.name}"
		err['type'] = "UnknownUse"
		return [result : 'Unknown!', errors : [err]]
	}


	//Checks if the guidelines for "Document/Literal-Wrapped" have been respected.
	private checkWrapped(List<BindingOperation> operations, PortType portType) {
		String result = "Document/Literal-Wrapped"

		operations.each {op ->
			if(!(portType.getOperation(op.name)?.input?.message)) return doclit
			def inputParts = portType.getOperation(op.name).input.message.parts
			//Rule 1: Only "ONE" Part Definition in the Input & Output Message in WSDL
			if(inputParts?.size() > 1) {
				result = doclit
			}
			inputParts.each { inPart->
				//Rule 2: "Part" Definitions should use element and not type
				if(inPart.typePN && !inPart.elementPN) {
					result = doclit
					return
				}
				//Rule 3: Input Wrapper Element name should match with Operation name
				if(inPart.element?.name  != op.name) result = doclit
			}
			if(!portType.getOperation(op.name).output?.message) return
			def outputParts = portType.getOperation(op.name).output.message.parts
			outputParts.each { outPart->
				//Rule 2: "Part" Definitions are wrapper elements
				if(outPart.type && !outPart.element) {
					result = doclit
					return
				}
				//Rule 4: <Output Wrapper Element Name> = <Operation Name> + "Response"
				if(outPart.element?.name  != "${op.name}Response") result = doclit
			}
		}
		result
	}
	
	private checkDocLitErrors(List<BindingOperation> operations, PortType	portType) {
		def errors = []
		operations.each {op ->
			//Check if the bindingOperation exists also in the portType
			if(!portType.getOperation(op.name)) {
				//TODO throw new OperationAccessException()
				def err = [:]
				err['message'] = "The operation '${op.name}' is missing in the portType '${portType.name}'."
				err['operation'] = op
				err['element'] = op
				err['type'] = "missingPortTypeOperation"
				errors << err
				return
			}
			//Check input soap body
			if(!op.input) throw new ModelAccessException("No input for binding operation '${op.name}'", op)
			def inputSoapBodyParts = op.input.bindingElements.grep(AbstractSOAPBody).partNames.flatten()
			if(inputSoapBodyParts.size() == 1) {
				errors.addAll(checkElementInPart(op.input.message))
				return
			}
			if(inputSoapBodyParts.size() > 1) {
				def err = [:]
				err['message'] = "The operation '${op.name}' uses more than one part in soap body of the input element."
				err['operation'] = op
				err['element'] = op.input
				err['type'] = "moreThanOnePart"
				errors << err
			} else {
				//Check if the referenced message in the operation uses only one part and if the part references an element.
				def opInputMessage = op.input.message
				errors.addAll(checkMessageParts(op, opInputMessage))
			}
			
			//Check output soap body
			if(!op.output) return
			def outputSoapBodyParts = op.output.bindingElements.grep(AbstractSOAPBody).partNames.flatten()
			if(outputSoapBodyParts.size() == 1) {
				errors.addAll(checkElementInPart(op.output.message))
				return
			}
			if(outputSoapBodyParts.size() > 1) {
				def err = [:]
				err['message'] = "The operation '${op.name}' uses more than one part in soap body of the output element."
				err['operation'] = op
				err['element'] = op.output
				err['type'] = "moreThanOnePart"
				errors << err
			} else {
				//Check if the referenced message in the operation uses only one part and if the part references an element.
				def opOutputMessage = op.output.message
				errors.addAll(checkMessageParts(op, opOutputMessage))
			}
		}
		errors
	}
	
	private checkMessageParts(BindingOperation op, Message message){
		def errors = []
		if(message.parts?.size() > 1) {
			def err = [:]
			err['message'] = "The operation '${op.name}' uses the message '${message.name}' which has more than one part."
			err['operation'] = op
			err['element'] = message
			err['type'] = "moreThanOnePart"
			errors << err
		}
		errors.addAll(checkElementInPart(message))
		errors
	}

	private def checkElementInPart(Message msg) {
		def errors = []
		msg.parts.each {part ->
			if(part.type && !part.element) {
				def err = [:]
				err['message'] = "The part of the message '${msg.name}' references a schema type instead of a schema element."
				err['element'] = msg
				err['type'] = "PartWithType"
				errors << err
			}
			if(!part.type && !part.element) {
				def err = [:]
						err['message'] = "The part of the message '${msg.name}' has no element reference."
						err['element'] = msg
						err['type'] = "PartWithNoRef"
						errors << err
			}
		}
		errors
	}

}
