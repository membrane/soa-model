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
import com.predic8.soamodel.CreatorContext 

import com.predic8.schema.restriction.*
import javax.xml.namespace.QName as JQName

class ComplexContent extends SchemaComponent {

  boolean mixed
  Derivation derivation

   protected parseAttributes(token, params){
    mixed = token.getAttributeValue( null , 'mixed')
   }

  protected parseChildren(token, child, params) {
    switch (child ){
      case 'extension' :
      derivation = new Extension(schema: schema) ; break
      case 'restriction' :
      derivation = new Restriction(schema: schema); break
    }
    derivation.parse(token, params) 
  }

  boolean hasExtension(){
    derivation instanceof Extension
  }

  boolean hasRestriction(){
    derivation instanceof Restriction
  }

  protected getElementName(){
    new JQName(Schema.SCHEMA_NS, 'complexContent')
  }

  def create(creator, CreatorContext ctx){
    creator.createComplexContent(this, ctx)
  }

  def compare(generator, other){
    generator.compareComplexContent(this, other)
  }
  
   
  
}