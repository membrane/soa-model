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

import com.predic8.soamodel.AbstractDiffGenerator;
import com.predic8.soamodel.CreatorContext 
import com.predic8.soamodel.DiffGeneratorContext;

import groovy.xml.*

import javax.xml.namespace.QName as JQName

class Attribute extends Declaration {

  String form
  String id
  QName ref
  String use
  SimpleType simpleType
	String defaultValue 
	String fixedValue  

  
  Attribute(){  }
  
  Attribute(String name, JQName type){
    this.name = name
    this.type = new QName(type.namespaceURI, type.localPart)
  }
	
		
  
  protected parseAttributes(token, params){
		super.parseAttributes(token, params)
		name = token.getAttributeValue( null , 'name')
		defaultValue = token.getAttributeValue( null , 'default')
		fixedValue = token.getAttributeValue( null , 'fixed')
    form = token.getAttributeValue( null , 'form')
    id = token.getAttributeValue( null , 'id')
    if (token.getAttributeValue( null , 'ref'))
      ref = getTypeQName(token.getAttributeValue( null , 'ref'))
    //if(ref && form) throw new RuntimeException('Form and ref attributes cannot be both present in an attribute element.')
    //if(ref && name) throw new RuntimeException('Name and ref attributes cannot be both present in an attribute element.')
    if (token.getAttributeValue( null , 'type'))
      type = getTypeQName(token.getAttributeValue( null , 'type'))
    //if(ref && type) throw new RuntimeException('Type and ref attributes cannot be both present in an attribute element.')
    use = token.getAttributeValue( null , 'use')?:'optional'
  }

  protected parseChildren(token, child, params){
    switch (child ){
			case 'annotation' :
  			annotation = new Annotation(schema: schema)
  			annotation.parse(token, params) ; break
      case 'simpleType' :
        simpleType = new SimpleType(schema:schema)
        simpleType.parse(token, params) ; break
    }
  }
  
  String getBuildInTypeNameLocal(){
    if(ref) return schema.getAttribute(ref).getBuildInTypeName() 
    simpleType.buildInTypeName
  }

  def create(creator, CreatorContext ctx){
    creator.createAttribute(this, ctx)
  }

  def compare(AbstractDiffGenerator generator, other, DiffGeneratorContext ctx = new DiffGeneratorContext()){
    generator.compareAttribute(this, other, ctx)
  }

  def getElementName(){
    'attribute'
  }
  
  String toString(){
    "attribute[name= $name, ref=$ref, type=$type]"
  }
  
}
