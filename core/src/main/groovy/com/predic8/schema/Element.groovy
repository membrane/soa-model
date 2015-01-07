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

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.predic8.schema.creator.*
import com.predic8.soamodel.AbstractDiffGenerator
import com.predic8.soamodel.CreatorContext
import com.predic8.soamodel.DiffGeneratorContext
import com.predic8.wadl.creator.JsonCreator
import com.predic8.wadl.creator.JsonCreatorContext
import com.predic8.wstool.creator.*
import com.predic8.xml.util.*

class Element extends Declaration {
  
  private static final Logger log = LoggerFactory.getLogger(Element.class)
  
  TypeDefinition embeddedType
  boolean toplevel = false
  Annotation annotation
	String refValue
  QName ref
  String minOccurs = 1
  String maxOccurs = 1
	String defaultValue //can only be used if the element's content is a simple type or text only
	String fixedValue //can only be used if the element's content is a simple type or text only
	String form // Possible values: qualified/unqualified. Cannot be used if element is top level
	boolean nillable = false
	Unique unique
  
  protected parseAttributes(token, params){
    super.parseAttributes(token, params)
    type = getTypeQName(token.getAttributeValue( null , 'type'))
    minOccurs = token.getAttributeValue( null , 'minOccurs') ?: 1
    maxOccurs = token.getAttributeValue( null , 'maxOccurs') ?: 1
    refValue = token.getAttributeValue( null , 'ref')
		ref = getTypeQName(refValue)
		// Element can have a default value OR a fixed value specified.
		defaultValue = token.getAttributeValue( null , 'default')
		fixedValue = token.getAttributeValue( null , 'fixed')
		nillable = (token.getAttributeValue( null , 'nillable') == 'true')
		form = token.getAttributeValue( null , 'form') 
    log.debug "element attribute parsed: [name=$name, type=$type, ref=$ref, nillable=$nillable]"
  }
  
  protected parseChildren(token, child, params){
		//		Can't use super.parseChildren(token, child, params) to parse annotation!
    switch (child ){
      case 'annotation' :
        annotation = new Annotation(schema: schema)
          annotation.parse(token, params) ; break
      case 'complexType' :
        embeddedType = new ComplexType(schema:schema)
          embeddedType.parse(token, params) ; break
      case 'simpleType' :
        embeddedType = new SimpleType(schema:schema, parent: this)
          embeddedType.parse(token, params) ; break
			case 'unique' :
				unique = new Unique(schema:schema, parent: this)
					unique.parse(token, params) ; break
    }
    log.debug "child [$child] of element [$name] parsed!"
  }
  
  protected getElementName(){
    'element'
  }
  
  def create(creator, CreatorContext ctx){
    creator.createElement(this, ctx.clone())
  }
  
  def compare(AbstractDiffGenerator generator, other, DiffGeneratorContext ctx = new DiffGeneratorContext()){
    generator.compareElement(this, other, ctx)
  }
  
  String getRequest(formParams){
    def writer = new StringWriter()
    create(new RequestCreator(builder:new MarkupBuilder(writer)),new RequestCreatorContext(path:"xpath:/",formParams:formParams))
    writer.toString()
  }
	
	String getAsJson(JsonCreatorContext ctx = new JsonCreatorContext()) {
		new JsonCreator().getElementAsJson(this)
	}
  
  String getHtmlForm(){
    def writer = new StringWriter()
    create(new FormCreator(builder:new MarkupBuilder(writer)),new FormCreatorContext(path:'xpath:/'))
    writer.toString()
  }
  
  String getBuildInTypeNameLocal(){
	  if (embeddedType)
	  	return embeddedType.buildInTypeName
		  
  	  // @TODO same for ref simpleTypes
  }
  
  ComplexType newComplexType(){
    embeddedType = new ComplexType(schema:schema)
  }
  
  String toString(){
    "element[name=$name,type=$type,ref=$ref,embeddedType=$embeddedType]"
  }
}
