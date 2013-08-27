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

import com.predic8.schema.*
import com.predic8.schema.restriction.facet.*
import com.predic8.schema.restriction.BaseRestriction
import com.predic8.schema.restriction.StringRestriction;
import com.predic8.soamodel.AbstractCreator
import com.predic8.wsdl.usage.OperationUseVisitorContext;

abstract class AbstractSchemaCreator <Context extends SchemaCreatorContext> extends AbstractCreator{

	void createSchema(Schema schema, Context ctx){

	}

	void createImport(Import imp, Context ctx){

	}

	void createElement(Element element, Context ctx) {
		def refType = element.schema.getType(element.type)
		if(refType && !(refType instanceof BuiltInSchemaType)) {
			refType.create(this, ctx)
		}
		else if(element.ref) {
			element.schema.getElement(element.ref).create(this, ctx)
		}
		element.embeddedType?.create(this, ctx)
	}

	void createComplexType(ComplexType complexType, Context ctx) {
		if(complexType.model) {
			complexType.model.create(this, ctx)
		}
	}

	void createGroup(Group group, Context ctx){
		group.model.create(this, ctx)
	}

	void createGroupRef(GroupRef groupRef, ctx) {
		Group group = groupRef.schema.getGroup(groupRef.ref)
		group.create(this, ctx)
	}

	void createSequence(Sequence sequence, Context ctx){
		sequence.particles.each {
			it.create(this, ctx)
		}
	}

	void createChoice(Choice choice, Context ctx){
		choice.particles.each {
			it.create(this, ctx)
		}
	}

	void createAll(All all, Context ctx){
		all.particles.each {
			it.create(this, ctx)
		}
	}

	void createPatternFacet(PatternFacet facet, ctx) {

	}

	void createEnumerationFacet(EnumerationFacet enumerationFacet, ctx){

	}

	void createLengthFacet(LengthFacet facet, ctx){

	}

	void createList(SchemaList list,Context ctx){
		throw new Exception("List not supported!")
		//list.simpleType.create(this, ctx)
	}

	void createMaxLengthFacet(MaxLengthFacet facet, Context ctx){

	}

	void createMinLengthFacet(MinLengthFacet facet, Context ctx){

	}

	void createUnion(Union union, Context ctx) {
		union.simpleTypes[0]?.create(this, ctx)
	}

	void createSimpleType(SimpleType simpleType, Context ctx){
		if ( simpleType.union ) simpleType.union.create(this, ctx)
		else if ( simpleType.list ) simpleType.list.create(this, ctx)
		else simpleType.restriction.create(this, ctx)
	}

	void createSimpleRestriction(BaseRestriction restriction, Context ctx) {
		throw new RuntimeException("createExtension not implemented yet in ${this.class}")
	}

	public void createStringRestriction(StringRestriction res, Context  ctx) {
		createSimpleRestriction(res, ctx)
	}

	void createExtension(Extension extension, Context ctx){
		throw new RuntimeException("createExtension not implemented yet in ${this.class}")
	}

	void createComplexContentRestriction(Restriction restriction, Context ctx){
		throw new RuntimeException("createComplexContentRestriction not implemented yet in ${this.class}")
	}

	void createAnnotation(Annotation annotation, Context ctx){

	}

	void createSimpleContent(SimpleContent simpleContent, Context ctx){
//		throw new RuntimeException("createSimpleContent in ${this.class} not implemented yet!")
		simpleContent.extension?.create(this, ctx) 
		simpleContent.restriction?.create(this, ctx) 
	}

	void createComplexContent(ComplexContent complexContent, Context ctx){
		if(complexContent.derivation) {
			complexContent.derivation.create(this, ctx)
		}
	}

	void createAny(Any any, Context  ctx){}

	void createAnyAttribute(AnyAttribute anyAttribute, Context  ctx){
		throw new RuntimeException("createAnyAttribute not implemented yet in ${this.class}")
	}

	protected getElementTagName(Element element, ctx){
		if(!element.toplevel && element.schema.elementFormDefault=="unqualified")
			return element.name
		else
			return "${getNSPrefix(element, ctx)}:${element.name}"
	}

	protected getNSPrefix(element, ctx){
		if(!element.prefix || element.prefix == 'tns') {
			if(ctx.declNS[element.namespaceUri]) return ctx.declNS[element.namespaceUri][0]
			return getUnusedPrefix("ns1", ctx)
		}
		element.prefix
	}

	protected getUnusedPrefix(prefix, ctx) {
		if(ctx.declNS.values()?.flatten()?.contains(prefix))
			return getUnusedPrefix(prefix[0..-2]+(prefix[-1].toInteger()+1), ctx)
		prefix
	}

	protected buildElement(ctx, params) {
		if ( !ctx.attrs ) ctx.attrs = [:]
		if ( !ctx.body ) ctx.body = {}
		if ( !ctx.text ) ctx.text = ''
		declNSifNeeded(getNSPrefix(params.element, params),params.element.namespaceUri,ctx.attrs,params)
		builder."${getElementTagName(params.element, params)}"(ctx.attrs, ctx.text) {ctx.body}
	}

	protected def declNSifNeeded(prefix,ns,attrs,ctx) {
		prefix = prefix ?: ''

		if (prefix == "xml") return
			if (!ctx.declNS[ns]) ctx.declNS[ns] = []
		if ( ctx.declNS[ns].contains(prefix) ) return
			if (!prefix && !ns) return
			attrs[prefix ? "xmlns:${prefix}" : "xmlns"] = ns
		ctx.declNS[ns] << prefix
		ctx.declNS[ns]
	}
}