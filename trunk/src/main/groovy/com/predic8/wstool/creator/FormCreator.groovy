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

import com.predic8.schema.*
import com.predic8.schema.Sequence as SchemaSequence
import com.predic8.schema.restriction.*;
import com.predic8.schema.restriction.facet.*
import com.predic8.schema.creator.*
import com.predic8.soamodel.CreatorContext 
import org.apache.commons.logging.*
import com.predic8.wsdl.BindingOperation;
import com.predic8.wsdl.soap11.SOAPBody as SOAP11Body
import com.predic8.wsdl.soap12.SOAPBody as SOAP12Body
import com.predic8.wsdl.soap11.SOAPHeader as SOAP11Header

class FormCreator extends AbstractSchemaCreator <FormCreatorContext>{
  
  private Log log = LogFactory.getLog(this.class)
  
  def definitions
  
  void createRequest(String portTypeName, String operationName, String bindingName) {
    def operation = definitions.getOperation(operationName, portTypeName)
    def bindingOperation = definitions.getBinding(bindingName).getOperation(operationName)
    
    def msg = operation.input.message
    getHeaderElements(bindingOperation).each{ header ->
      definitions.getElement(msg.getPart(header.part).element).create(this, new FormCreatorContext(formParams:'',path:"xpath:/"))
    }
    def body = bindingOperation.input.bindingElements.find{it instanceof SOAP11Body || it instanceof SOAP12Body}
    msg.parts.findAll{it in body.parts}.each{
      definitions.getElement(it.element).create(this, new FormCreatorContext(formParams:'',path:"xpath:/"))
    }
  }
  
  private getHeaderElements(BindingOperation bindingOperation){
    bindingOperation.input.bindingElements.findAll{it instanceof SOAP11Header}
  }
  
  void createElement(Element element, FormCreatorContext ctx) {
    def type = element.schema.getType(element.type) ?: element.embeddedType 
    if(type){
      def newCtx = ctx.clone()
      newCtx.element = element
      type.create(this, newCtx)
      return
    }
    writeInputForBuildInType(element, ctx)
  }
  
  protected writeInputForBuildInType(item,ctx){
    
    def tbodyAttrs = [:]
    def aAttrs = [:]
    def cardinality = ''
    
    tbodyAttrs.id = getFieldName(item, ctx)
    aAttrs.id = "add:${getFieldName(item, ctx)}"
    aAttrs.onclick = "add(\"${getFieldName(item, ctx)}\")"
    if(item instanceof Attribute) {
      tbodyAttrs.maxOccurs = 1
    } else {
      tbodyAttrs.minOccurs = item.minOccurs
      tbodyAttrs.maxOccurs = item.maxOccurs
      cardinality = "(${item.minOccurs}..${item.maxOccurs})"
    }
    builder.table() {
      tbody(tbodyAttrs){
        tr {
          td( colspan:2) {
            b("${item.name} $cardinality")
            if(addPossible(item)) {
              aAttrs.style = "visibility:${addVisibility(item) ? 'visible' : 'hidden'}"
              a(aAttrs){
                img(src:'file://C:/temp/FormCreatorTest/icons/add.png', alt:'add')
              }
            }
          }
        }
        (getMinOccurs(tbodyAttrs.minOccurs)).times{
          tr{
            td(width:'100'){ writeInputField(item, ctx)}
          }
        }
      }
    }
  }
  
  int getMinOccurs(minOccurs){
    minOccurs = minOccurs?.toInteger()
    if(minOccurs < 2) return 1
    return minOccurs
  }
  
  protected writeInputField(item, FormCreatorContext ctx){
    //Unclear specification! If there is an empty element without any type definition, we will dump an empty element.//
    if(!item.type && !item.embeddedType) {
      return
    }
    String buildinTypeName = item.type?.localPart ?: item.embeddedType.buildInType
    def attrs = [name:getFieldName(item, ctx), type: 'text']
    String info
    switch (buildinTypeName) {
      case 'string' :
        if(ctx.attrs) attrs['label'] = ctx.attrs['label']
        break;
      case 'boolean' :
        attrs += [type:'checkbox', value:'true']
        break
      case 'int' :
        attrs += [maxLength:11,size:12]
        info = 'int'
        break
      case 'float' :
        attrs += [maxLength:30,size:12]
        info = 'float (e.g. 0.5)'
        break
      case 'double' :
        attrs += [maxLength:30,size:11]
        info = 'double (e.g. 0.55)'
        break
      case 'date' :
        attrs += [maxLength:10,size:11]
        info = 'date (YYYY-MM-DD)'
        break
      case 'dateTime' :
        attrs += [maxLength:29,size:30]
        info = 'dateTime (e.g. 2008-10-01T13:20:00.000+01:00)'
        break
      default :
        if(ctx.attrs) attrs['label'] = ctx.attrs['label']
        attrs += [type:'text']
        log.debug "Type: ${buildinTypeName} is not supported yet"
    }
    builder.input(attrs)
    writeRemove(item, ctx)
    builder.span(info)
    builder.br()
  }
  
  protected writeRemove(item, ctx){
    if(!removePossible(item)) return
    
    def attrs = [:]
    attrs.id = "remove:${getFieldName(item, ctx)}"
    attrs.onclick = "removeFirst(\"${getFieldName(item, ctx)}\")"
    attrs.style = "visibility:${removePossible(item) ? 'visible' : 'collapse'}"
    builder.a(attrs){
      img(src:'file://C:/temp/FormCreatorTest/icons/delete.png', alt:'remove')
    }
  }
  
  void createComplexType(ComplexType complexType, FormCreatorContext ctx){
    def newCtx = ctx.clone()
    newCtx.path = "${ctx.path}${ctx.element.name}/"
    builder.div(class:'complexType'){
      b("${ctx.element.name} (${ctx.element.minOccurs}..${ctx.element.maxOccurs})")
      br()
      writeAttributes(complexType, newCtx)
      complexType.model?.create(this, newCtx)
    }
  }
  
  public void createSimpleRestriction(BaseRestriction res, FormCreatorContext  ctx) {
    if(res.enumerationFacet){
      res.enumerationFacet.create(this, ctx)
      return
    }
    ctx.attrs = [:]
    if(res.minLengthFacet && res.maxLengthFacet) {
      ctx.attrs['label'] = "Should be between ${res.minLengthFacet.value} and ${res.maxLengthFacet.value} characters."
    } else if(res.minLengthFacet) {
      ctx.attrs['label'] = "Should be at least ${res.minLengthFacet.value} characters."
    } else if(res.maxLengthFacet){
      ctx.attrs['label'] = "Should have at most ${res.minLengthFacet.value} characters."
    }
    if(ctx.element) {
      writeInputField(ctx.element ,  ctx)
    }
  }
  
  public void createStringRestriction(StringRestriction res, SchemaCreatorContext ctx) {
    if(res.enumerationFacet){
      res.enumerationFacet.create(this, ctx)
      return
    }
    ctx.attrs = [:]
    if(res.minLengthFacet && res.maxLengthFacet) {
      ctx.attrs['label'] = "Should be between ${res.minLengthFacet.value} and ${res.maxLengthFacet.value} characters."
    } else if(res.minLengthFacet) {
      ctx.attrs['label'] = "Should be at least ${res.minLengthFacet.value} characters."
    } else if(res.maxLengthFacet){
      ctx.attrs['label'] = "Should have at most ${res.minLengthFacet.value} characters."
    }
    //@todo Patternfacet, maxInclusive, lengthFacet must be implemented! 
    if(ctx.element) {
      writeInputForBuildInType(ctx.element, ctx)
    }
  }
  
  void createExtension(Extension extension, SchemaCreatorContext ctx){
    extension.schema.getType(extension?.base).model?.create(this, ctx)
    writeAttributes(extension.schema.getType(extension?.base), ctx)
    extension.model?.create(this, ctx)
    extension.attributes.each{ writeInputForBuildInType(it,ctx) }
  }
  
  void createComplexContentRestriction(Restriction restriction, SchemaCreatorContext ctx){
    restriction.model?.create(this, newCtx)
    restriction.attributes.each{ writeInputForBuildInType(it,ctx) }
  }
  
  def writeAttributes(ComplexType complexType, ctx){
    complexType.attributes.each{ writeInputForBuildInType(it,ctx) }
  }
  
  void createEnumerationFacet(EnumerationFacet facet, SchemaCreatorContext ctx){
    builder.b("${ctx.element.name}:")
    builder.select(name:"${ctx.path}${ctx.element.name}"){
      facet.values.each{ item ->
        builder.option(item)
      }
    }
    builder.br()
  }
  
  void createAnnotation(Annotation annotation, SchemaCreatorContext ctx) {
    annotation.documentations.each{
      builder.p(it.content)
    }
  }
  
  protected boolean addVisibility(item){
    if(item instanceof Attribute) return false
    if(item.maxOccurs instanceof String && item.maxOccurs=="unbounded")
      return true
    (item.maxOccurs.toInteger()-1) > item.minOccurs.toInteger()
  }
  
  private boolean addPossible(item){
    if(item instanceof Attribute)
      return item.use != 'required'
    if(item.maxOccurs instanceof String && item.maxOccurs=="unbounded")
      return true
    return (item.maxOccurs.toInteger() - item.minOccurs.toInteger()) >= 1
  }
  
  private boolean removePossible(item){
    if(item instanceof Element)
      return item.minOccurs.toInteger()==0
    item.use != 'required'
  }
  
  private getFieldName(comp, ctx){
    if(comp instanceof Attribute) {
      return "${ctx.path}@${comp.name}"
    } else {
      return "${ctx.path}${comp.name}" + (addPossible(comp)? '[1]' : '')
    }
  }
}
