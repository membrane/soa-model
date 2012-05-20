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

package com.predic8.schema.restriction;

import javax.xml.namespace.QName as JQName
import groovy.xml.QName as GQName

import com.predic8.wstool.creator.*
import com.predic8.schema.restriction.facet.*
import com.predic8.schema.*
import com.predic8.soamodel.*

class BaseRestriction  extends SchemaComponent {
  
  SimpleType simpleType
  
  GQName base
  List<Facet> facets = []
  
  AnyAttribute anyAttribute
  
  protected parseChildren(token, child, params){
    switch (child ){
      case 'enumeration':
      def enumeration = facets.grep{it instanceof EnumerationFacet}[0]
      if(!enumeration) {
        enumeration = new EnumerationFacet()
        facets << enumeration
      }
      enumeration.values << token.getAttributeValue( null , 'value') ; break
      case 'length' :
      facets << new LengthFacet(value : token.getAttributeValue( null , 'value').toInteger()) ; break
      case 'maxLength' :
      facets << new MaxLengthFacet(value : token.getAttributeValue( null , 'value').toInteger()) ; break
      case 'minLength' :
      facets << new MinLengthFacet(value : token.getAttributeValue( null , 'value').toInteger()) ; break
      case 'pattern' :
      facets << new PatternFacet(value :token.getAttributeValue( null , 'value')) ; break
      case 'whiteSpace' :
      facets << new WhiteSpaceFacet(value :token.getAttributeValue( null , 'value')) ; break
      case 'maxInclusive' :
      facets << new MaxInclusiveFacet(value : token.getAttributeValue( null , 'value')) ; break
      case 'maxExclusive' :
      facets << new MaxExclusiveFacet(value : token.getAttributeValue( null , 'value')) ; break
      case 'minInclusive' :
      facets << new MinInclusiveFacet(value : token.getAttributeValue( null , 'value')) ; break
      case 'minExclusive' :
      facets << new MinExclusiveFacet(value : token.getAttributeValue( null , 'value')) ; break
      case 'totalDigits' :
      facets << new TotalDigitsFacet(value : new BigDecimal(token.getAttributeValue( null , 'value'))) ; break
      case 'fractionDigits' :
      facets << new FractionDigits(value : new BigDecimal(token.getAttributeValue( null , 'value'))) ; break
      case 'anyAttribute' :
      anyAttribute = new AnyAttribute(schema: schema)
      anyAttribute.parse(token, params) ; break
    }
  }
  
  public boolean equals(obj) {
    obj && getClass() == obj.getClass() && base == obj.base && facets == obj.facets
  }
  
  protected getElementName(){
    new JQName(Schema.SCHEMA_NS, 'restriction')
  }
  
  LengthFacet getLengthFacet(){
    facets.find{it instanceof LengthFacet}
  }
  
  EnumerationFacet getEnumerationFacet() {
    facets.find{it instanceof EnumerationFacet}
  }
  
  Boolean hasEnumerationFacet() {
    enumerationFacet ?: false
  }
  
  MaxInclusiveFacet getMaxInclusiveFacet(){
    facets.find{it instanceof MaxInclusiveFacet}
  }
  
  MaxLengthFacet getMaxLengthFacet(){
    facets.find{it instanceof MaxLengthFacet}
  }
  
  MinLengthFacet getMinLengthFacet(){
    facets.find{it instanceof MinLengthFacet}
  }
  
  String getBuildInTypeName(){
    base.localPart  
  }
  
  def create(creator, CreatorContext ctx){
    creator.createSimpleRestriction(this, ctx)
  }
  
  def compare( generator, other ) {
    generator.compareSimpleRestriction(this, other)
  }
	
  def createXML(builder,element, path, formParams){}
  
  def createXML(builder, path, formParams){}

  def String toString() {
    "[base=$base,facets=$facets]"
  }
}
