

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
 
package com.predic8.schema

import com.predic8.schema.restriction.QNameRestriction;
import com.predic8.soamodel.AbstractDiffGenerator;
import com.predic8.soamodel.CreatorContext;
import com.predic8.soamodel.DiffGeneratorContext;

import groovy.xml.QName

class BuiltInSchemaType extends TypeDefinition {

	String type	String namespace
	QName qname
	
  protected getElementName() {
	  'built-in-schema-type'
  }
	
	List<QName> getSuperTypes(ctx=[]){
		[]
	}
	
	String getType() {
		qname.localPart
	}
	
	String getNamespace() {
		qname.namespaceURI
	}
	
	/**
	 * Some creators use their own create method for built-in-types. 
	 */
	def create(creator, CreatorContext ctx) {
		creator.createBuiltInSchemaType(this, ctx)
	}
	
	def compare(AbstractDiffGenerator generator, other, DiffGeneratorContext ctx = new DiffGeneratorContext()){
		generator.compareBuiltInSchemaType(this, other, ctx)
	}
	
	String toString(){
		"BuiltInSchemaType[qname=$qname]"
	}
}
