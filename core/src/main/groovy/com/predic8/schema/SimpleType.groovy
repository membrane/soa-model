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

package com.predic8.schema;

import static com.predic8.soamodel.Consts.SCHEMA_NS
import groovy.xml.*

import com.predic8.schema.restriction.BaseRestriction
import com.predic8.schema.restriction.RestrictionUtil
import com.predic8.schema.restriction.Restriction as SimpleTypeRestriction
import com.predic8.soamodel.AbstractDiffGenerator
import com.predic8.soamodel.CreatorContext
import com.predic8.soamodel.DiffGeneratorContext
import com.predic8.wstool.creator.*

class SimpleType extends TypeDefinition {

	BaseRestriction restriction
	Union union
	SchemaList list

	protected parseChildren(token, child, params){
		super.parseChildren(token, child, params)
		switch (child ){
			case 'union' :
				union = new Union(schema: schema)
					union.parse(token, params) ; break
			case 'list' :
				list = new SchemaList(schema: schema)
					list.parse(token, params) ; break
			case 'restriction' :
				def base = token.getAttributeValue( null , 'base') ? getTypeQName(token.getAttributeValue( null , 'base')) : null
				if(base) {
					def type = base.localPart
					if(base.namespaceURI == SCHEMA_NS){
						restriction = RestrictionUtil.getRestriction(type, [base: base, parentSimpleType : this])
					} else {
						restriction = new BaseRestriction(parentSimpleType : this , base : base, schema: schema)
					}
				} else {
					//In case of restriction without base attribte!
					restriction = new SimpleTypeRestriction(parentSimpleType: this, schema: schema)
				}
				restriction.parse(token, params)
		}
	}

	List<QName> getSuperTypes(ctx=[]){
		if(!restriction) return []
		if(restriction && schema.getType(restriction.base)){
			//To avoid cycling type definition
			if(restriction.base in ctx) return []
			return [restriction.base]+ schema.getType(restriction.base).getSuperTypes(ctx << restriction.base)
		}
		[restriction?.base]?: []
	}

	public boolean equals(obj) {
		obj && ( this.is(obj) ||
				getClass() == obj.getClass() &&
				restriction == obj.restriction &&
				union == obj.union &&
				list == obj.list )
	}

	protected getElementName(){
		'simpleType'
	}

	String getBuildInTypeName(){
		if(list) return list.buildInType
		if(restriction) return restriction.buildInTypeName
	}

	def create(creator, CreatorContext ctx){
		creator.createSimpleType(this, ctx.clone())
	}

	def compare(AbstractDiffGenerator generator, other, DiffGeneratorContext ctx = new DiffGeneratorContext()){
		generator.compareSimpleType(this, other, ctx)
	}

	String toString(){
		"SimpleType[qname=$qname,restriction=$restriction]"
	}
}
