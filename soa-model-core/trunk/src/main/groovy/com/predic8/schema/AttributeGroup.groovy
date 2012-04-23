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

import java.util.List;

import groovy.xml.*
import com.predic8.soamodel.CreatorContext
import com.predic8.wstool.creator.*
import com.predic8.xml.util.PrefixedName
import javax.xml.stream.*

class AttributeGroup extends SchemaComponent{

  QName ref
  List<Attribute> attributes = []
  List<AttributeGroup> attributeGroups = []

  protected parseAttributes(token, params){
    super.parseAttributes(token, params)
    ref = getTypeQName(token.getAttributeValue( null , 'ref'))
  }

  protected parseChildren(token, child, params){
    switch (child ){
      case 'attribute' :
        def attribute = new Attribute(schema:schema)
        attribute.parse(token, params)
          attributes << attribute ; break
      case 'attributeGroup' :
        def attributeGroup = new AttributeGroup(schema:schema)
        attributeGroup.parse(token, params)
          attributeGroups << attributeGroup ; break
      case 'anyAttribute' :
        def anyAttr = new AnyAttribute(schema: schema)
        anyAttr.parse(token, params)
          attributes << anyAttr ; break
    }
  }

  Attribute getAttribute(String name){
    attributes.find{it.name == name}
  }

  List<Attribute> getAttributesFromRef(){
    if(!ref) return
      schema.getAttributeGroup(ref).allAttributes?.flatten()
  }

  List<Attribute> getAllAttributes(){
    def res = attributes ?: attributesFromRef
    attributeGroups.each { res << it.allAttributes }
    res.flatten()
  }

  protected getElementName(){
    'attributeGroup'
  }

  def create(creator, CreatorContext ctx){
    creator.createAttributeGroup(this, ctx.clone())
  }

  def compare(generator, other){
    generator.compareGroups(this, other)
  }

  String toString(){
    "attributeGroup[name= $name, ref=$ref,attributes=$attributes, attributeGroups=$attributeGroups]"
  }
}
