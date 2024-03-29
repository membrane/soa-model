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

import groovy.xml.*
import groovy.namespace.*

import javax.xml.namespace.QName as JQName;

import com.predic8.wstool.creator.*
import com.predic8.soamodel.AbstractDiffGenerator;
import com.predic8.soamodel.CreatorContext;
import com.predic8.soamodel.DiffGeneratorContext;

class ComplexType extends TypeDefinition {
  
  SchemaComponent model
  String abstractAttr 
	
	protected parseAttributes(token, params){
		super.parseAttributes(token, params)
		abstractAttr = token.getAttributeValue( null , 'abstract')
	}
	
  protected parseChildren(token, child, params){
    super.parseChildren(token, child, params)
    switch (child ){
      case 'sequence' :
      model = new Sequence(schema:schema)
      model.parse(token, params) ; break
      case 'choice':
      model = new Choice(schema:schema)
      model.parse(token, params) ; break
      case 'all' :
      model = new All(schema:schema)
      model.parse(token, params) ; break
      case 'group' :
      def ref = token.getAttributeValue( null , 'ref')
      if(ref){
        model = new GroupRef(schema : schema, ref : getTypeQName(ref))
      }else{
        model = new Group(schema:schema)
      }
      model.parse(token, params)
      break
      case 'simpleContent' :
      model = new SimpleContent(schema: schema)
      model.parse(token, params) ; break
      case 'complexContent' :
      model = new ComplexContent(schema: schema)
      model.parse(token, params) ; break
    }
  }

  public getElementName(){
    'complexType'
  }

  SchemaComponent getModel(){
    if(model instanceof QName){
      return schema.getType(model)
    }
    model
  }
	
	/**
	 * If C is in a ComplexType hierarchy like: A <- B <- C ,
	 * calling C.getSuperTypes() returns [B, A]  
	 * @return the list of derivated CompexTypes and SimpleTypes   
	 */
	List<QName> getSuperTypes(ctx = []){
		//TODO The super type should be resolved as object, otherwise an exception should be thrown.
		if(model instanceof ComplexContent || model instanceof SimpleContent){
			QName base = model.derivation.base ?: ((model.extension ?: model.restriction).base)
			//To avoid cycling type definition
			if(base in ctx) return []
			return schema.getType(base) ? [base] + schema.getType(base)?.getSuperTypes(ctx << base) : [base]
		}
		[]
	}
	
  def create(creator, CreatorContext ctx){
    creator.createComplexType(this, ctx.clone())
  }

  def compare(AbstractDiffGenerator generator, other, DiffGeneratorContext ctx = new DiffGeneratorContext()){
    generator.compareComplexType(this, other, ctx)
  }
  
  Sequence getSequence(){
    if(model instanceof Sequence) return model
    null
  }
  
  String getBuildInTypeName(){
    if(model instanceof SimpleContent) return model.restriction.base.localPart
  }
  
  void add(Attribute attr){
    attr.parent = this
    attributes << attr
  }
  
  Attribute newAttribute(String name, JQName type){
    def attr = new Attribute(name, type)
    attr.schema = schema
    attr.parent = this
    attributes << attr
    attr
  }
  
  Sequence newSequence(){
    model = new Sequence(schema:schema, parent: this)
    model
  } 
  
  String toString(){
    "complexType[qname=$qname,model=$model]"
  }
  
  boolean equals(Object obj) {
    if(this.is(obj)) {
      return true
    }
    if( !obj || (obj.getClass() != this.getClass())) {
      return false
    }
    if(obj.schema.targetNamespace != schema.targetNamespace) {
      return false
    }
    if(obj.qname != qname) {
      return false
    }
    true
  }

  int hashCode()
  {
    (qname ?: "abc").hashCode() + (schema ?: "xsd").hashCode()
  }
}
