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


package com.predic8.wstool.creator;

import groovy.xml.*

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.predic8.schema.*
import com.predic8.schema.creator.*
import com.predic8.schema.restriction.BaseRestriction
import com.predic8.schema.restriction.facet.*
import com.predic8.soamodel.Consts
import com.predic8.soamodel.ModelAccessException

class RequestTemplateCreator extends AbstractSchemaCreator <RequestTemplateCreatorContext> {
  
  private static final Logger log = LoggerFactory.getLogger(RequestTemplateCreator.class)
  
  
  void createElement(Element element, RequestTemplateCreatorContext ctx){
		
    ctx.elements.add(element)
    if(ctx.elements.findAll{it == ctx.element}.size() >= ctx.maxRecursionDepth) {
      yield("""\n<!-- Element ${ctx.element.name} has been defined recursivly and will not be created more than ${ctx.maxRecursionDepth} times.
				For more repetition increase the  maxRecursionDepth value in the CreatorContext.-->""")
      return
    }
		
    if(element.minOccurs == element.maxOccurs && element.maxOccurs != '1') yield("\n<!-- must occur exact ${element.minOccurs} times -->")
    else if(element.minOccurs == '0' && element.maxOccurs == '1') yield("\n<!-- optional -->")
    else if (element.minOccurs != element.maxOccurs) yield("\n<!-- from ${element.minOccurs} to ${element.maxOccurs} -->")
		if(element.nillable) yield("\n<!-- This element may be left empty if xsi:nil='true' is set. -->")
    log.debug "Element ${element?.name}"
    if ( element.ref ) {
      element.schema.getElement(element.ref).create(this,ctx)
      return
    }
    ctx.element = element
		
		if(element.fixedValue != null){ 	// != null for empty strings
			yield("\n<!-- this element has a fixed value -->")
			builder."${getElementTagName(element, ctx)}"(element.fixedValue, )
			return
		}
		if(element.defaultValue != null){ 	// != null for empty strings
			yield("\n<!-- this element has a default value -->")
			builder."${getElementTagName(element, ctx)}"(element.defaultValue, )
			return
		}
		
    if(element.embeddedType){
      element.embeddedType.create(this, ctx)
      return
    }
		if(element.type) {
			def refType = element.schema.getType(element.type)
	    if(refType && !(refType instanceof BuiltInSchemaType)){
				refType.create(this, ctx)
	      return
	    }
	    if(refType && (refType instanceof BuiltInSchemaType)){
	      def attrs = [:]
	      declNSifNeeded(getNSPrefix(element, ctx),element.namespaceUri,attrs,ctx)
	      if(element.type.localPart=='dateTime') yield('<!--dateTime-->')
	      builder."${getElementTagName(element, ctx)}"(TemplateUtil.getTemplateValue(element.type),attrs)
	    }
		}
    if(!element.type && !element.embeddedType) {
      builder."${getElementTagName(element, ctx)}"()
    }
  }
	
  void createComplexType(ComplexType complexType, RequestTemplateCreatorContext ctx){
    log.debug "ComplexType ${complexType?.name}"
    def schema = complexType.schema
    ctx.path = "${ctx.path}${ctx.element.name}/"
		
		if(complexType.model instanceof ComplexContent && complexType.model.hasRestriction()){
			complexType.model?.create(this, ctx)
			complexType.anyAttribute?.create(this, ctx)
			return
		}
		
    def attrs = [:]
		declNSifNeeded(getNSPrefix(ctx.element, ctx),ctx.element.namespaceUri,attrs,ctx)
    attrs.putAll(createAttributes(complexType, ctx))
    builder."${getElementTagName(ctx.element, ctx)}"(attrs){
      complexType.model?.create(this, ctx)
      complexType.anyAttribute?.create(this, ctx)
    }
  }
  
  Map createAttributes(Object obj, RequestTemplateCreatorContext ctx){
    def res = [:]
    def attrs = obj.allAttributes
    attrs.each{
      def attr = it.ref ? obj.schema.getAttribute(it.ref) : it
      if(attr.fixedValue) {
        res[attr.name] = attr.fixedValue
        return
      }
      if(attr.simpleType){
        if(attr.simpleType.restriction){
          res[attr.name] = TemplateUtil.getTemplateValue(attr.simpleType.restriction.base)
        }
      } else {
			declNSifNeeded(getNSPrefix(attr, ctx),attr.namespaceUri,res,ctx)
			res["${getNSPrefix(attr, ctx)}:${attr.name}"] = TemplateUtil.getTemplateValue(attr.type)
      }
    }
    res
  }
  
  void createSimpleRestriction(BaseRestriction restriction, RequestTemplateCreatorContext ctx){
    if(restriction.facets){
      yield("\n<!-- ")
      restriction.facets.each{
        it.create(this, ctx)
        if(it == restriction.facets.last()) return 
        yield(", ")
      }
      yield(" -->")
    }
    buildElement(ctx,text:'???')
  }
  
  void createEnumerationFacet(EnumerationFacet facet, RequestTemplateCreatorContext ctx){
    yield("possible value: ${facet.value}")
  }
  
  void createLengthFacet(LengthFacet facet, RequestTemplateCreatorContext ctx){
    yield("maximum length: ${facet.value}")
  }
  
  void createTotalDigitsFacet(TotalDigitsFacet facet, RequestTemplateCreatorContext ctx){
    yield("maximum number of digits: ${facet.value}")
  }
  
  void createFractionDigits(FractionDigits fdigits, RequestTemplateCreatorContext ctx) {
    yield("fraction digits: ${fdigits.value}")
  }
  
  void createPatternFacet(PatternFacet facet, RequestTemplateCreatorContext ctx){
    yield("Pattern: ${facet.value}")
  }
  
  void createMaxLengthFacet(MaxLengthFacet facet, RequestTemplateCreatorContext ctx){
    yield("Max Length: ${facet.value}")
  }
  
  void createMinLengthFacet(MinLengthFacet facet, RequestTemplateCreatorContext ctx){
    yield("Min Lenght: ${facet.value}")
  }
  
  void createMaxInclusiveFacet(MaxInclusiveFacet facet, RequestTemplateCreatorContext ctx){
    yield("Max Inclusive: ${facet.value}")
  }
  
  void createMinInclusiveFacet(MinInclusiveFacet facet, RequestTemplateCreatorContext ctx){
    yield("Min Inclusive: ${facet.value}")
  }
  
  void createMaxExclusiveFacet(MaxExclusiveFacet facet, RequestTemplateCreatorContext ctx){
  	yield("Max Exclusive: ${facet.value}")
  }
  
  void createMinExclusiveFacet(MinExclusiveFacet facet, RequestTemplateCreatorContext ctx){
  	yield("Min Exclusive: ${facet.value}")
  }
  
  void createWhiteSpaceFacet(WhiteSpaceFacet facet, RequestTemplateCreatorContext ctx){
  	yield("White Space: ${facet.value}")
  }
  
  void createExtension(Extension extension, RequestTemplateCreatorContext ctx){
    if(extension.base.namespaceURI.equals(Consts.SCHEMA_NS)){
      yield("${TemplateUtil.getTemplateValue(extension.base)}")
      return
    }
    def baseType = extension.schema.getType(extension.base)
    if(!baseType) throw new ModelAccessException("Could not find the referenced type '${extension.basePN}' in schema '${extension.schema.targetNamespace}'.")
    if(baseType instanceof SimpleType) return
    baseType.model?.create(this, ctx)
    extension.anyAttribute?.create(this,ctx)
    extension.model?.create(this,ctx)
  }
  
  void createAnnotation(Annotation annotation, RequestTemplateCreatorContext ctx) {
    annotation.documentations.each{ yield("<!--${it.content}-->") }
  }
  
  void createPart(part, RequestTemplateCreatorContext ctx){
		if(!part.type) throw new ModelAccessException("There is no type information for the part '${part.name}' although the referencing operation was declared as RPC style.")
    if(part.type instanceof ComplexType || part.type instanceof SimpleType){
      part.type.create(this, ctx)
      return
    }
    if(part.type instanceof BuiltInSchemaType){
      if(part.type.qname.localPart=='dateTime') yield('<!--dateTime-->')
      builder."${part.name}"(TemplateUtil.getTemplateValue(part.type.qname))
    }
  }
  
  void createSimpleContent(SimpleContent simpleContent, RequestTemplateCreatorContext ctx){
    simpleContent.extension?.create(this, ctx)
    simpleContent.restriction?.create(this, ctx)
  }
  
  void createAny(Any any, RequestTemplateCreatorContext  ctx){
		/*TODO
		 * Change the yield calls to comment, like the followin:
		 * yield("<!-- This element can be extended by any element from ${any.namespace ?: 'any'} namespace -->")
		 */
		builder.mkp.comment "This element can be extended by any element from ${any.namespace ?: 'any'} namespace"
  }
  
  void createAnyAttribute(AnyAttribute anyAttribute, RequestTemplateCreatorContext ctx){
    yield("<!-- This element can be extended by any attribute from ${anyAttribute.namespace ?: 'any'} namespace -->")
  }
	
	void createComplexContentRestriction(Restriction restriction, RequestTemplateCreatorContext ctx){
		if(restriction.base) restriction.schema.getType(restriction.base).create(this, ctx)
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
  
  private yield(s) {
    new MarkupBuilderHelper(builder).yieldUnescaped(s)
  }
}
