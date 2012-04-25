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

import com.predic8.schema.creator.SchemaCreatorContext;
import groovy.xml.*

import com.predic8.soamodel.Consts
import com.predic8.schema.*
import com.predic8.schema.creator.*
import com.predic8.schema.restriction.facet.*
import com.predic8.schema.restriction.BaseRestriction
import com.predic8.wsdl.Part
import org.apache.commons.logging.*

class RequestTemplateCreator extends AbstractSchemaCreator <RequestTemplateCreatorContext> {
  
  private Log log = LogFactory.getLog(this.class)
  
  
  void createElement(Element element, RequestTemplateCreatorContext ctx){
    ctx.elements.add(element)
    if(ctx.elements.findAll{it == ctx.element}.size() >= ctx.maxRecursionDepth) {
      yield("\n<!-- Element ${ctx.element.name} has been defined recursivly and will not be created more than ${ctx.maxRecursionDepth} times. -->")
      return
    }
    if(element.minOccurs == element.maxOccurs && element.maxOccurs != '1') yield("\n<!-- must occur exact ${element.minOccurs} times -->")
    else if(element.minOccurs == '0' && element.maxOccurs == '1') yield("\n<!-- optional -->")
    else if (element.minOccurs != element.maxOccurs) yield("\n<!-- from ${element.minOccurs} to ${element.maxOccurs} -->")
    log.debug "Element ${element?.name}"
    if ( element.ref ) {
      element.schema.getElement(element.ref).create(this,ctx)
      return
    }
    ctx.element = element
    if(element.embeddedType){
      element.embeddedType.create(this, ctx)
      return
    }
    def refType = element.schema.getType(element.type)
    if(refType){
      refType.create(this, ctx)
      return
    }
    if(element.type && element.type.namespaceURI.equals(Consts.SCHEMA_NS)){
      def attrs = [:]
      declNSifNeeded('ns1',element.schema.targetNamespace,attrs,ctx)
      if(element.type.localPart=='dateTime') new MarkupBuilderHelper(builder).yieldUnescaped('<!--dateTime-->')
      builder."${getElementTagName(element)}"(TemplateUtil.getTemplateValue(element.type),attrs)
    }
    if(!element.type && !element.embeddedType) {
      builder."${getElementTagName(element)}"()
    }
  }
  
  void createComplexType(ComplexType complexType, RequestTemplateCreatorContext ctx){
    log.debug "ComplexType ${complexType?.name}"
    def schema = complexType.schema
    ctx.path = "${ctx.path}${ctx.element.name}/"
    def attrs = [:]
    declNSifNeeded('ns1',complexType.schema.targetNamespace,attrs,ctx)
    attrs.putAll(createAttributes(complexType, ctx))
    new MarkupBuilderHelper(builder).yieldUnescaped('<!--dateTime-->')
    builder."${getElementTagName(ctx.element)}"(attrs){
      complexType.model?.create(this, ctx)
      complexType.anyAttribute?.create(this, ctx)
    }
  }
  
  Map createAttributes(Object obj, RequestTemplateCreatorContext ctx){
    def res = [:]
    def attrs = obj.allAttributes
    attrs.each{
      def attr = it.ref ? obj.schema.getAttribute(it.ref) : it
      if(attr.fixed) {
        res[attr.name] = attr.fixed
        return
      }
      if(attr.simpleType){
        if(attr.simpleType.restriction){
          res[attr.name] = TemplateUtil.getTemplateValue(attr.simpleType.restriction.base)
        }
      } else {
        res[attr.name] = TemplateUtil.getTemplateValue(attr.type)
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
    yield("possible values: ${facet.values.join(',')}")
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
    yield("MaxLenght: ${facet.value}")
  }
  
  void createMinLengthFacet(MinLengthFacet facet, RequestTemplateCreatorContext ctx){
    yield("MinLenght: ${facet.value}")
  }
  
  void createMaxInclusiveFacet(MaxInclusiveFacet facet, RequestTemplateCreatorContext ctx){
    yield("MaxInclusiveFacet: ${facet.value}")
  }
  
  void createMinInclusiveFacet(MinInclusiveFacet facet, RequestTemplateCreatorContext ctx){
    yield("MinInclusiveFacet: ${facet.value}")
  }
  
  void createExtension(Extension extension, RequestTemplateCreatorContext ctx){
    if(extension.base.namespaceURI.equals(Consts.SCHEMA_NS)){
      yield("${TemplateUtil.getTemplateValue(extension.base)}")
      return
    }
    def baseType = extension.schema.getType(extension.base)
    if(!baseType) throw new Exception("BaseType: $baseType of extension: $extension not found!")
    if(baseType instanceof SimpleType) return
    baseType.model?.create(this, ctx)
    extension.anyAttribute?.create(this,ctx)
    extension.model?.create(this,ctx)
  }
  
  void createAnnotation(Annotation annotation, RequestTemplateCreatorContext ctx) {
    annotation.documentations.each{ yield("<!--${it.content}-->") }
  }
  
  void createPart(part, RequestTemplateCreatorContext ctx){
    def refType = part.definitions.getSchema(part.type.namespaceURI)?.getType(part.type)
    if(refType){
      refType.create(this, ctx)
      return
    }
    if(part.type && part.type.namespaceURI.equals(Consts.SCHEMA_NS)){
      if(part.type.localPart=='dateTime') yield('<!--dateTime-->')
      builder."${part.name}"(TemplateUtil.getTemplateValue(part.type))
    }
  }
  
  void createSimpleContent(SimpleContent simpleContent, RequestTemplateCreatorContext ctx){
    simpleContent.extension?.create(this, ctx)
    simpleContent.restriction?.create(this, ctx)
  }
  
  void createAny(Any any, RequestTemplateCreatorContext  ctx){
    yield("\n<!-- This element can be extended by any element from ${any.namespace ?: 'any'} namespace -->")
  }
  
  void createAnyAttribute(AnyAttribute anyAttribute, RequestTemplateCreatorContext  ctx){
    yield("\n<!-- This element can be extended by any attribute from ${anyAttribute.namespace ?: 'any'} namespace -->")
  }
  
  private yield(s) {
    new MarkupBuilderHelper(builder).yieldUnescaped(s)
  }
}
