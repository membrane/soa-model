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

package com.predic8.wsdl.usage;

import com.predic8.schema.*
import com.predic8.schema.creator.AbstractSchemaCreator;
import com.predic8.schema.restriction.BaseRestriction
import com.predic8.schema.restriction.facet.*
import com.predic8.wsdl.AbstractPortTypeMessage
import com.predic8.wsdl.Message;
import com.predic8.wsdl.Operation
import com.predic8.wsdl.PortType;

class OperationUseVisitor extends AbstractSchemaCreator<OperationUseVisitorContext> {

	public OperationUseVisitorContext visitSchema4Operation(Operation op, PortType portType, OperationUseVisitorContext ctx) {
		if(op.input) {
			op.input.message.parts.each {part ->
				ctx.visited = []
				ctx.opUsage = new OperationUsage(operation: op, portType: portType, input: true)
				part.element? (part.element.create(this, ctx)) : (part.type?.create(this, ctx))
			}
		}
		
		if(op.output) {
			op.output.message.parts.each {part ->
				ctx.visited = []
				ctx.opUsage = new OperationUsage(operation: op, portType: portType, output: true)
				part.element? part.element.create(this, ctx) : part.type?.create(this, ctx)
			}
		}

		op.faults.each {fault ->
			fault.message.parts.each {part ->
				ctx.visited = []
				ctx.opUsage = new OperationUsage(operation: op, portType: portType, fault: true)
				part.element? part.element.create(this, ctx) : part.type?.create(this, ctx)
			}
		}

		ctx
	}

	public void createElement(Element element, OperationUseVisitorContext ctx) {
		if(ctx.visited.contains(element)) return
		ctx.visited << element
		if(element.name) ctx.updateElements(element)
		super.createElement(element, ctx)
	}

	public void createComplexType(ComplexType complexType, OperationUseVisitorContext ctx) {
		if(ctx.visited.contains(complexType)) return
		ctx.visited << complexType
		if(complexType.name) ctx.updateCompexTypes(complexType)
		super.createComplexType(complexType, ctx)
	}

	public void createSimpleType(SimpleType simpleType, OperationUseVisitorContext ctx) {
		if(ctx.visited.contains(simpleType)) return
		ctx.visited << simpleType
		if(simpleType.name) ctx.updateSimpleTypes(simpleType)
		if(! simpleType.restriction?.base instanceof BuiltInSchemaType)
			simpleType.schema.getType(simpleType.restriction.base).create(this, ctx)
	}

	void createExtension(Extension extension, OperationUseVisitorContext ctx){
		extension.schema.getType(extension.base).create(this, ctx)
	}

	void createComplexContentRestriction(Restriction restriction, OperationUseVisitorContext ctx){
		restriction.schema.getType(restriction.base).create(this, ctx)
	}

	void createSimpleRestriction(BaseRestriction restriction, OperationUseVisitorContext ctx){
		restriction.schema.getType(restriction.base).create(this, ctx)
	}

	private void update() {
	}

	public void createBuiltInSchemaType(BuiltInSchemaType bist, OperationUseVisitorContext ctx) {
		//Not needed!
	}
}