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

import javax.xml.namespace.QName as JQName;

import groovy.xml.*

import org.apache.commons.logging.*

import com.predic8.wstool.creator.*
import com.predic8.schema.creator.*;
import com.predic8.soamodel.CreatorContext
import com.predic8.xml.util.*

class Element extends Declaration {
  
  private Log log = LogFactory.getLog(this.class)
  
  TypeDefinition embeddedType
  boolean toplevel = false
  Annotation annotation
  QName ref
  String minOccurs = 1
  String maxOccurs = 1
  
  protected parseAttributes(token, params){
    super.parseAttributes(token, params)
    type = getTypeQName(token.getAttributeValue( null , 'type'))
    minOccurs = token.getAttributeValue( null , 'minOccurs') ?: 1
    maxOccurs = token.getAttributeValue( null , 'maxOccurs') ?: 1
    ref = getTypeQName(token.getAttributeValue( null , 'ref'))
    log.debug "element attribute parsed: [name=$name, type=$type, ref=$ref]"
  }
  
  protected parseChildren(token, child, params){
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
    }
    log.debug "child [$child] of element [$name] parsed!"
  }
  
  protected getElementName(){
    'element'
  }
  
  def create(creator, CreatorContext ctx){
    creator.createElement(this, ctx.clone())
  }
  
  def compare(generator, other){
    generator.compareElement(this, other)
  }
  
  String getRequest(formParams){
    def writer = new StringWriter()
    create(new RequestCreator(builder:new MarkupBuilder(writer)),[path:"xpath:/",formParams:formParams])
    writer.toString()
  }
  
  String getForm(){
    def writer = new StringWriter()
    create(new FormCreator(builder:new MarkupBuilder(writer)),new FormCreatorContext(path:'xpath:/'))
    writer.toString()
  }
  
  String getBuildInTypeNameLocal(){
    embeddedType.buildInTypeName
  }
  
  ComplexType newComplexType(){
    embeddedType = new ComplexType(schema:schema)
  }
  
  String toString(){
    "element[name=$name,type=$type,ref=$ref,embeddedType=$embeddedType]"
  }
}
