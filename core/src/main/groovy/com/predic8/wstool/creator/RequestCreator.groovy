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

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.predic8.schema.creator.*
import com.predic8.schema.*
import com.predic8.wsdl.Part
import com.predic8.schema.restriction.facet.*
import com.predic8.schema.restriction.BaseRestriction

import groovy.xml.MarkupBuilderHelper

class RequestCreator extends AbstractSchemaCreator<RequestCreatorContext> {
  
  private static final Logger log = LoggerFactory.getLogger(RequestCreator.class)
  
  void createElement(Element element, RequestCreatorContext ctx){
		
  	ctx.elements.add(element)
		
    if(element.fixedValue){
			yield("\n<!-- The value of this element is fixed and can not be modified. -->")
			builder."${getElementTagName(element, ctx)}"(element.fixedValue)
			return
		}
		
		if(element.embeddedType){
      log.debug "embeddedType found"
      element.embeddedType.create(this, ctx.clone(element))
      return
    }
    
    if(element.ref ) {
      element.schema.getElement(element.ref).create(this,ctx.clone(element))
      return
    }
    
		if(element.type) {
	    def refType = element.schema.getType(element.type)
			//If refType is a built-in schema type, the value of the element has to be created using the createBuildInElement()
	    if(refType && !(refType instanceof BuiltInSchemaType)){
	      log.debug "refType : $refType"
	      refType.create(this, ctx.clone(element))
	      return
	    }
		}
    createBuildInElement(element, ctx)
  }
  
  private createBuildInElement(Element element, RequestCreatorContext ctx) {
    def attrs = [:]
		declNSifNeeded(getNSPrefix(element, ctx),element.namespaceUri,attrs,ctx)
	
		/**There is a problem with the next line if two or more elements were used/declared more than one time in a xpath expression.
		  *See https://groups.google.com/forum/?hl=en&fromgroups=#!topic/soa-model/CVBErhVbdek
		  *
			*This makes also problems if the element name is also used in upper levels.
		  */
    def entries = ctx.formParams.findAll{(it.key.split('/')[0..-2] + (it.key.split('/')[-1]  - ~/\[\d\]/)).join('/') == "${ctx.path}${element.name}"}

		/*The next line causes unnecessary elements be created without xpath expression!
    if(element.type?.localPart=='boolean' && !entries) entries["${ctx.path}${element.name}"] = 'false'
	  This could be a fix:*/
	  if(!entries && element.minOccurs == '0') return

    entries.keySet().sort{it}.each {
      builder."${getElementTagName(element, ctx)}"(entries[it],attrs)
    }
    if (!entries) {
			if(element.defaultValue){
				yield("\n<!-- This element has a default value. -->")
				builder."${getElementTagName(element, ctx)}"(element.defaultValue, attrs)
				return
			}
	  //This is the case, where the element is required and has not be declared in the xpath expression!
	  yield("\n<!-- This element is required and should be filled. -->")
      builder."${getElementTagName(element, ctx)}"(null,attrs)
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
		declNSifNeeded(getNSPrefix(ctx.element, ctx),ctx.element.namespaceUri,attrs,ctx)
    complexType.allAttributes.each {
      if(ctx.formParams["${ctx.path}@${it.name}"])
        attrs[it.name] = ctx.formParams["${ctx.path}@${it.name}"]
    }
    builder."${getElementTagName(ctx.element, ctx)}"(attrs){
      complexType.model?.create(this, ctx)
    }
  }
  
  public getElementXpaths(ctx){
    def es = []
    ctx.formParams.keySet().each {
		def wholePath = "${ctx.path}${ctx.element.name}";
		wholePath = wholePath.replaceAll(/\[/, "\\\\[");
		wholePath = wholePath.replaceAll(/\]/, "\\\\]");
		def e = it =~ /$wholePath(?:\[\d+\])*\/.*?/
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
	
	@Override
	protected getElementTagName(Element element, ctx){
		/*Only if the element is from the same namespace as the
		* top-level-element of the request, it doesn't need a prefix.
		*/
		if(!element.toplevel && element.schema.elementFormDefault=="unqualified" && ctx.elements[0].namespaceUri == element.namespaceUri)
			return element.name
		else
			return "${getNSPrefix(element, ctx)}:${element.name}"
	}
	
  private getFormParamValue(ctx) {
    ctx.formParams["${ctx.path}${ctx.element.name}"]
  }
  
  private yield(s) {
	  new MarkupBuilderHelper(builder).yieldUnescaped(s)
  }
}
