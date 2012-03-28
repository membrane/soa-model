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

package com.predic8.schema.creator;

import com.predic8.schema.*
import com.predic8.schema.Sequence as SchemaSequence
import com.predic8.schema.restriction.facet.*
import com.predic8.schema.restriction.BaseRestriction
import com.predic8.schema.restriction.StringRestriction;
import com.predic8.soamodel.AbstractCreator

abstract class AbstractSchemaCreator <Context extends SchemaCreatorContext> extends AbstractCreator{
  
  void createSchema(Schema schema, Context ctx){}
  
  void createImport(Import imp, Context ctx){}
  
  abstract void createElement(Element element, Context ctx)
  
  abstract void createComplexType(ComplexType complexType, Context ctx)
  
  void createGroup(Group group, Context ctx){
    group.model.create(this, ctx)
  }
  
  void createGroupRef(GroupRef groupRef, ctx) {
    groupRef.schema.getGroup(groupRef.ref).create(this, ctx)
  }
  
  void createSequence(SchemaSequence sequence, Context ctx){
    sequence.particles.each {
      it.create(this, ctx)
    }
  }
  
  void createChoice(Choice choice, Context ctx){
    choice.particles.each {
      it.create(this, ctx)
    }
  }
  
  void createAll(All all, Context ctx){
    all.particles.each {
      it.create(this, ctx)
    }
  }
  
  void createPatternFacet(PatternFacet facet, ctx) {}
  
  void createEnumerationFacet(EnumerationFacet enumerationFacet, ctx){}
  
  void createLengthFacet(LengthFacet facet, ctx){}
  
  //TODO
  void createList(SchemaList list,Context ctx){
    throw new Exception("List not supported!")
    //list.simpleType.create(this, ctx)
  }
  
  void createMaxLengthFacet(MaxLengthFacet facet, Context ctx){}
  
  void createMinLengthFacet(MinLengthFacet facet, Context ctx){}
  
  //TODO
  void createUnion(Union union, Context ctx) {
    union.simpleTyps[0]?.create(this, ctx)
  }
  
  void createSimpleType(SimpleType simpleType, Context ctx){
    if ( simpleType.union ) simpleType.union.create(this, ctx)
    else if ( simpleType.list ) simpleType.list.create(this, ctx)
    else simpleType.restriction.create(this, ctx)
  }
  
  abstract void createSimpleRestriction(BaseRestriction restriction, Context ctx)
  
  public void createStringRestriction(StringRestriction res, Context  ctx) {
    createSimpleRestriction(res, ctx)
  }
  
  //TODO
  void createExtension(Extension extension, Context ctx){
    throw new RuntimeException("createExtension not implemented yet in ${this.class}")
  }
  
  //TODO 
  void createComplexContentRestriction(Restriction restriction, Context ctx){
    throw new RuntimeException("createComplexContentRestriction not implemented yet in ${this.class}")
  }
  
  void createAnnotation(Annotation annotation, Context ctx){}
  
  //TODO
  void createSimpleContent(SimpleContent simpleContent, Context ctx){
     throw new RuntimeException("createSimpleContent in ${this.class} not implemented yet!")
  }
  
  void createComplexContent(ComplexContent complexContent, Context ctx){
    complexContent.derivation?.create(this, ctx)
  }
  
  protected getElementTagName(Element element){
    if(!element.toplevel && element.schema.elementFormDefault=="unqualified")
      return element.name
    else
      return "ns1:${element.name}"
  }
  
  protected buildElement(params, ctx) {
    if ( !params.attrs ) params.attrs = [:]
    if ( !params.body ) params.body = {}
    if ( !params.text ) params.text = ''
    params.attrs['xmlns:ns1']=ctx.element.schema.targetNamespace //TODO Use declNSifNeeded.
    builder."${getElementTagName(ctx.element)}"(params.attrs, params.text) {params.body}
  }
  
  protected def declNSifNeeded(prefix,ns,attrs,ctx) {
    prefix = prefix ?: ''

    if (prefix == "xml") return
    if (!ctx.declNS[ns]) ctx.declNS[ns] = []
    if ( ctx.declNS[ns].contains(prefix) ) return
    
    attrs[prefix ? "xmlns:${prefix}" : "xmlns"] = ns
    ctx.declNS[ns] << prefix
  }
}