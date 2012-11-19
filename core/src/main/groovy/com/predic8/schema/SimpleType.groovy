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

import com.predic8.soamodel.CreatorContext 
import com.predic8.wstool.creator.*
import com.predic8.schema.restriction.*

import groovy.xml.*

class SimpleType extends TypeDefinition {
  
  BaseRestriction restriction
  Union union
  SchemaList list

  protected parseChildren(token, child, params){
    super.parseChildren(token, child, params)
    switch (child ){
      case 'union' :
      union = new Union(schema: schema)
      union.parse(token, params) ; break
      case 'list' :
      list = new SchemaList(schema: schema)
      list.parse(token, params) ; break
      case 'restriction' :
      def base = getTypeQName(token.getAttributeValue( null , 'base'))
      def type = base.localPart
      if(schema.getNamespace(base.prefix) == Schema.SCHEMA_NS){
        restriction = RestrictionUtil.getRestriction(type, [base: base, simpleType : this])
      } else {
        restriction = new BaseRestriction(simpleType : this , base : base)
      }
      restriction.parse(token, params) 
    }
  }

  public boolean equals(obj) {
    obj && ( this.is(obj) ||
             getClass() == obj.getClass() &&
             restriction == obj.restriction &&
             union == obj.union &&
             list == obj.list )
  }
  
  protected getElementName(){
    'simpleType'
  }
  
  String getBuildInTypeName(){
    if(list) return list.buildInType
    if(restriction) return restriction.buildInTypeName
  }
  
  def create(creator, CreatorContext ctx){
    creator.createSimpleType(this, ctx.clone())
  }

  def compare(generator, other){
    generator.compareSimpleType(this, other)
  }
  
  String toString(){
    "SimpleType[qname=$qname,restriction=$restriction]"
  }
}
