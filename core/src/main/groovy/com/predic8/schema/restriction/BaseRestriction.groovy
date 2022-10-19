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

import groovy.namespace.QName as GQName

import com.predic8.wstool.creator.*
import com.predic8.schema.restriction.facet.*
import com.predic8.schema.*
import com.predic8.soamodel.*

import static com.predic8.soamodel.Consts.SCHEMA_NS

class BaseRestriction  extends SchemaComponent {
  
  SimpleType parentSimpleType
  SimpleType childSimpleType
	
  
  GQName base
  List<Facet> facets = []
  
  AnyAttribute anyAttribute
  
  protected parseChildren(token, child, ctx){
    super.parseAttributes(token, ctx)
    switch (child ){
			case 'simpleType' :
			childSimpleType = new SimpleType(schema: schema)
			childSimpleType.parse(token, ctx) ; break
      case 'enumeration' :
		  def facet = new EnumerationFacet(schema: schema)
		  facet.parse(token, ctx)
		  facets << facet ; break
      case 'length' :
		  def facet = new LengthFacet()
		  facet.parse(token, ctx)
		  facets << facet ; break
      case 'maxLength' :
		  def facet = new MaxLengthFacet()
		  facet.parse(token, ctx)
		  facets << facet ; break
      case 'minLength' :
		  def facet = new MinLengthFacet()
		  facet.parse(token, ctx)
		  facets << facet ; break
      case 'pattern' :
		  def facet = new PatternFacet()
		  facet.parse(token, ctx)
		  facets << facet ; break
      case 'whiteSpace' :
		  def facet = new WhiteSpaceFacet()
		  facet.parse(token, ctx)
		  facets << facet ; break
      case 'maxInclusive' :
		  def facet = new MaxInclusiveFacet()
		  facet.parse(token, ctx)
		  facets << facet ; break
      case 'maxExclusive' :
		  def facet = new MaxExclusiveFacet()
		  facet.parse(token, ctx)
		  facets << facet ; break
      case 'minInclusive' :
		  def facet = new MinInclusiveFacet()
		  facet.parse(token, ctx)
		  facets << facet ; break
      case 'minExclusive' :
		  def facet = new MinExclusiveFacet()
		  facet.parse(token, ctx)
		  facets << facet ; break
      case 'totalDigits' :
		  def facet = new TotalDigitsFacet()
		  facet.parse(token, ctx)
		  facets << facet ; break
      case 'fractionDigits' :
		  def facet = new FractionDigits()
		  facet.parse(token, ctx)
		  facets << facet ; break
      case 'anyAttribute' :
	      anyAttribute = new AnyAttribute(schema: schema)
	      anyAttribute.parse(token, ctx) ; break
    }
  }
  
  public boolean equals(obj) {
    obj && getClass() == obj.getClass() && facets == obj.facets && base == obj.base &&  childSimpleType == obj.childSimpleType
  }
  
  public getElementName(){
    new JQName(SCHEMA_NS, 'restriction')
  }
  
  LengthFacet getLengthFacet(){
    facets.find{it instanceof LengthFacet}
  }
  
  List<EnumerationFacet> getEnumerationFacets() {
    facets.findAll{it instanceof EnumerationFacet}
  }
  
  Boolean hasEnumerationFacet() {
    enumerationFacets ?: false
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
	
	public void setBase(qName) {
		if(qName instanceof JQName) {
			base = new GQName(qName.namespaceURI, qName.localPart, qName.prefix)
			return
		}
		base = qName
	}
  
  String getBuildInTypeName(){
    base?.localPart 
  }
  
  def create(creator, CreatorContext ctx){
    creator.createSimpleRestriction(this, ctx)
  }
  
  def compare(AbstractDiffGenerator generator, other, DiffGeneratorContext ctx = new DiffGeneratorContext()){
    generator.compareSimpleRestriction(this, other, ctx)
  }
	
  def createXML(builder,element, path, formParams){}
  
  def createXML(builder, path, formParams){}

  def String toString() {
    "Restriction[base=$base,facets=$facets]"
  }
}
