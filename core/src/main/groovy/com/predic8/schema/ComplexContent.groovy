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
import com.predic8.soamodel.AbstractDiffGenerator;
import com.predic8.soamodel.CreatorContext 
import com.predic8.soamodel.DiffGeneratorContext;
import com.predic8.schema.restriction.*

import javax.xml.namespace.QName as JQName

import static com.predic8.soamodel.Consts.SCHEMA_NS

class ComplexContent extends SchemaComponent {

  boolean mixed
  Derivation derivation
  BaseRestriction restriction

   protected parseAttributes(token, params){
		 mixed = token.getAttributeValue( null , 'mixed')
   }

  protected parseChildren(token, child, params) {
    switch (child ){
	  case 'annotation' :
		annotation = new Annotation(schema: schema)
		annotation.parse(token, params) ;
		return;
      case 'extension' :
      	derivation = new Extension(schema: schema) ; 
		break;
      case 'restriction' :
		def base = null
		  if (token.getAttributeValue( null , 'base'))
		 	 base = getTypeQName(token.getAttributeValue( null , 'base'))
		if(base) {
	      def type = base.localPart
		  if(base.namespaceURI == SCHEMA_NS){
			  // Are we parsing the schema of XML schema?
			  // e.g. the file schema/XSD Schema/schemas.xsd
			  // as in the AnySimpleTypeTest ?
			  if (schema.targetNamespace == SCHEMA_NS) {
			  	derivation = new Restriction(base : base, schema: schema) 
				derivation.schema = schema
				break;
			  } else {
			  	// It is a build in type from XML Schema
			    restriction = RestrictionUtil.getRestriction(type, [base: base])
			    restriction.schema = schema
				break;
			  }
		  } else {
			derivation = new Restriction(base : base, schema: schema)  // Restriction can be Derivation if there are children e.g. sequence
			break;
		  }
	    } else {
		  derivation = new Restriction(schema: schema)  // Restriction can be Derivation if there are children e.g. sequence
	    }
		break;
		
	  default: throw new RuntimeException("Invalid child element '$child' in complexContent. Possible elements are 'annotation', 'extension' or 'restriction'.")
    }
	
	// One of both get parsed
	restriction?.parse(token, params)
	derivation?.parse(token, params)
  }

  boolean hasExtension(){
    derivation instanceof Extension
  }

  boolean hasRestriction(){
    derivation instanceof Restriction
  }

  protected getElementName(){
    new JQName(SCHEMA_NS, 'complexContent')
  }

  def create(creator, CreatorContext ctx){
    creator.createComplexContent(this, ctx)
  }

  def compare(AbstractDiffGenerator generator, other, DiffGeneratorContext ctx = new DiffGeneratorContext()){
    generator.compareComplexContent(this, other, ctx)
  }
  
   
  
}