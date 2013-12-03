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

package com.predic8.schema.creator;

import groovy.xml.MarkupBuilder

import com.predic8.schema.*

class SchemaSubsetVisitor extends AbstractSchemaCreator <SchemaCreatorContext>{

	Schema createSchema4Element(Element element, SchemaCreatorContext ctx = new SchemaCreatorContext()){
		ctx.subSchema = new Schema(element.schema.targetNamespace)
		ctx.subSchema.elements << element
		element.create(this, ctx)
		return ctx.subSchema
	}

	void createComplexType(ComplexType complexType, SchemaCreatorContext ctx) {
		/*To avoid creating cycling complexTypes return if CT already known.*/
		if(complexType in ctx.subSchema.complexTypes) return
		/*ComplexType should only be created, if it is not embedded and has a name.*/
		if(complexType.name) ctx.subSchema.complexTypes << complexType
		if(complexType.model) complexType.model.create(this, ctx)
	}
	
	void createSimpleType(SimpleType simpleType, SchemaCreatorContext ctx){
		/*SimpleType should only be created, if it is not embedded and has a name.*/
		if(simpleType.name) ctx.subSchema.simpleTypes << simpleType
		if ( simpleType.restriction ) simpleType.restriction.create(this, ctx)
	}
	
	void createBuiltInSchemaType(BuiltInSchemaType type, SchemaCreatorContext ctx){}
	
	void createExtension(Extension extension, SchemaCreatorContext ctx){
		if(extension.base) extension.schema.getType(extension.base).create(this, ctx)
	}
	
	String getSchemaAsString(Element element) {
		def strWriter = new StringWriter()
		def creator = new SchemaCreator(builder : new MarkupBuilder(strWriter))
		createSchema4Element(element).create(creator, new SchemaCreatorContext())
		strWriter.toString()
	}
}