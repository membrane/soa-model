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

package com.predic8.schema

import java.util.List;

import com.predic8.xml.util.PrefixedName
import groovy.xml.*

abstract class TypeDefinition extends SchemaComponent {

  QName qname
  List<Attribute> attributes = []
  List<AttributeGroup> attributeGroups = []

  protected parseAttributes(token, params){
    name = token.getAttributeValue( null , 'name')
    if (name) {
      def preName = new PrefixedName(name)
      qname = new QName(schema.targetNamespace, preName.localName)
    }
  }

  protected parseChildren(token, child, params){
    super.parseChildren(token, child, params)
    switch (child ){
      case 'attribute' :
      def attr = new Attribute(schema: schema)
      attr.parse(token, params)
      attributes << attr ; break
      case 'attributeGroup' :
      def attributeGroup = new AttributeGroup(schema: schema)
      attributeGroup.parse(token, params)
      attributeGroups << attributeGroup ; break
    }
  }
  
  List<Attribute> getAllAttributes(){
    def attrs = []
    attrs.addAll(attributes)
    attributeGroups.each {
      attrs.addAll(it.allAttributes)
    }
    if(!metaClass.hasProperty(this, 'model') || !model) return attrs //simpleType do not have a model
    if(model.metaClass.hasProperty(model, 'derivation')){
      if(model.derivation){
        attrs.addAll(model.derivation.allAttributes)
        attrs.addAll(schema.getType(model.derivation.base)?.getAllAttributes()?:[])
      }
    }
    attrs
  }

  Attribute getAttribute(name) {
    attributes.find{ it.name == name }
  }
  
  AttributeGroup getAttributeGroup(String name){
    attributeGroups.find{it.name == name}
  }
}

