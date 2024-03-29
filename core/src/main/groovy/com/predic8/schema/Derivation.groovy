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

import javax.xml.namespace.QName as JQName

import com.predic8.xml.util.PrefixedName;

import groovy.namespace.QName

abstract class Derivation extends SchemaComponent{

  QName base
  List<Attribute> attributes = []
  List<AttributeGroup> attributeGroups = []
  SchemaComponent model
  AnyAttribute anyAttribute
	PrefixedName basePN

  protected parseAttributes(token, params){
    basePN = new PrefixedName(token.getAttributeValue( null , 'base'))
  }

  protected parseChildren(token, child, params) {
    switch (child ){
      case 'sequence' :
        model = new Sequence(schema:schema)
          model.parse(token, params) ; break
      case 'choice':
        model = new Choice(schema:schema)
          model.parse(token, params) ; break
      case 'all' :
        model = new All(schema:schema)
          model.parse(token, params) ; break
      case 'group' :
        def ref = token.getAttributeValue( null , 'ref')
        if(ref){
          model = new GroupRef(schema : schema, ref : getTypeQName(ref))
        }else{
          model = new Group(schema:schema)
        }
        model.parse(token, params)
        break
      case 'attribute' :
        def attr = new Attribute(schema: schema)
        attr.parse(token, params)
        attributes << attr ; break
      case 'attributeGroup' :
        def attributeGroup = new AttributeGroup(schema: schema)
        attributeGroup.parse(token, params)
          attributeGroups << attributeGroup ; break
      case 'anyAttribute' :
        anyAttribute = new AnyAttribute(schema: schema)
        anyAttribute.parse(token, params) ; break
    }
  }
	
	QName getBase() {
		if(base) return base
		base = getQNameForPN(basePN)
	}

  List<Attribute> getAllAttributes(){
    def attrs = []
    attrs.addAll(attributes)
    attributeGroups.each {
      attrs.addAll(it.allAttributes)
    }
    attrs
  }
	
}

