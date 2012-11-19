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

package com.predic8.wstool.creator

import org.apache.commons.logging.*

import com.predic8.schema.creator.*
import com.predic8.schema.*
import com.predic8.wsdl.Part
import com.predic8.schema.restriction.facet.*
import com.predic8.schema.restriction.BaseRestriction
import groovy.xml.MarkupBuilderHelper

class RequestCreator extends AbstractSchemaCreator<RequestCreatorContext> {
  
  def log = LogFactory.getLog(this.class)
  
  void createElement(Element element, RequestCreatorContext ctx){
    if(element.embeddedType){
      log.debug "embeddedType found"
      element.embeddedType.create(this, ctx.clone(element))
      return
    }
    
    if ( element.ref ) {
      element.schema.getElement(element.ref).create(this,ctx.clone(element))
      return
    }
    
    def refType = element.schema.getType(element.type)
    if(refType){
      log.debug "refType : $refType"
      refType.create(this, ctx.clone(element))
      return
    }
    createBuildInElement(element, ctx)
  }
  
  private createBuildInElement(Element element, RequestCreatorContext ctx) {
    def attrs = [:]
    declNSifNeeded('ns1',element.schema.targetNamespace, attrs, ctx)
	//This makes problems if the element name is also used in upper levels.
    def entries = ctx.formParams.findAll{(it.key.split('/')[0..-2] + (it.key.split('/')[-1]  - ~/\[\d\]/)).join('/') == "${ctx.path}${element.name}"}

		/*The next line causes unnecessary elements be created without xpath expression!
    if(element.type?.localPart=='boolean' && !entries) entries["${ctx.path}${element.name}"] = 'false'
	  This could be a fix:*/
	  if(!entries && element.minOccurs == '0') return

    entries.keySet().sort{it}.each {
      builder."${getElementTagName(element)}"(entries[it],attrs)
    }
    if (!entries) {
	  //This is the case, where the element is required and has not be declared in the xpath expression!
	  yield("\n<!-- This element is required and should be filled. -->")
      builder."${getElementTagName(element)}"(null,attrs)
    }
  }
  
  void createComplexType(ComplexType type, RequestCreatorContext ctx){
    getElementXpaths(ctx).each {
      def newCtx = ctx.clone()
      newCtx.path = it
      createElementFromCT(type, newCtx)
    }
    if(!getElementXpaths(ctx)) {
      if(ctx.element.minOccurs == '0') {
        return
      }
      def newCtx = ctx.clone()
      newCtx.path = ctx.path + ctx.element.name + '/'
      createElementFromCT(type, newCtx)
    }
  }
  
  private createElementFromCT(complexType, ctx){
    def attrs = [:]
    declNSifNeeded('ns1',complexType.schema.targetNamespace, attrs, ctx)
    complexType.allAttributes.each {
      if(ctx.formParams["${ctx.path}@${it.name}"])
        attrs[it.name] = ctx.formParams["${ctx.path}@${it.name}"]
    }
    builder."${getElementTagName(ctx.element)}"(attrs){
      complexType.model?.create(this, ctx)
    }
  }
  
  public getElementXpaths(ctx){
    def es = []
    ctx.formParams.keySet().each {
      def e = it =~ /${ctx.path}${ctx.element.name}.*?\//
      if (e)  es << e[0]
    }
    es.unique()
  }
  
  void createSimpleRestriction(BaseRestriction rest, RequestCreatorContext ctx) {
    buildElement(ctx,text:getFormParamValue(ctx))
  }
  
  void createExtension(Extension extension, RequestCreatorContext ctx){
    extension.schema.getType(extension?.base).model?.create(this, ctx)
    extension.model?.create(this, ctx)
  }
  
  void createComplexContentRestriction(Restriction restriction, RequestCreatorContext ctx){
    restriction.model?.create(this, ctx)
  }
  
  void createPart(Part part, RequestCreatorContext ctx){
    builder."${part.name}"(ctx.formParams["${ctx.path}${part.name}"])
  }

  private getFormParamValue(ctx) {
    ctx.formParams["${ctx.path}${ctx.element.name}"]
  }
  
  private yield(s) {
	  new MarkupBuilderHelper(builder).yieldUnescaped(s)
  }
}
