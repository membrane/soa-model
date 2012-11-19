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

class DummySchemaCreator extends AbstractSchemaCreator<SchemaCreatorContext> {
 
  void createSchema(Schema schema, SchemaCreatorContext context){
    println "createSchema"
  }
  
  void createElement(Element element, SchemaCreatorContext  context){
    println "createElement"
  }
  
  void createComplexType(ComplexType complexType, SchemaCreatorContext  context){
    println "createComplexType"    
  }
  
  void createSequence(SchemaSequence sequence, SchemaCreatorContext  context){
    println "createSequence"    
  }
  
  void createGroup(Group group, SchemaCreatorContext  context) {
    println "createGroup"
  }

  void createGroupRef(GroupRef groupRef, SchemaCreatorContext  context) {
    println "createGroupRef"
  }

  void createAll(All all, SchemaCreatorContext  context) {
    println "createAll"
  }
  
  void createPatternFacet(PatternFacet facet, SchemaCreatorContext  context){
    println "createPatternFacet"
  }
  void createEnumerationFacet(EnumerationFacet facet, SchemaCreatorContext  context){
    println "createEnumerationFacet"  
  }
   
  void createLengthFacet(LengthFacet facet, SchemaCreatorContext  context){
    println "createLengthFacet"
  }
  
  void createMaxLengthFacet(MaxLengthFacet facet, SchemaCreatorContext  context){
    println "createMaxLengthFacet"
  }

	void createFractionDigits(FractionDigits fdigits, SchemaCreatorContext  context) {
		println "createFractionDigits"
	}	

  void createMinLengthFacet(MinLengthFacet facet, SchemaCreatorContext  context){
    println "createMinLengthFacet"
  }
  
  void createSimpleType(SimpleType simpleType, SchemaCreatorContext  context){
    println "createSimpleType"
  }
  
  void createList(SchemaList list, SchemaCreatorContext  context){
    println "createList"
  }

  void createUnion(Union union, SchemaCreatorContext  context) {
    println "createUnion"
  }
  
  void createSimpleRestriction(BaseRestriction restriction, SchemaCreatorContext  context){
    println "createSimpleRestriction"
  }

  void createComplexRestriction(Restriction restriction, SchemaCreatorContext  context){
    println "createComplexRestriction"
  }

  void createExtension(Extension extension, SchemaCreatorContext  context) {
    println "createExtension"
  }
  

  void createAnnotation(Annotation annotation, SchemaCreatorContext  context){
    println "createAnnotation"
  }
  
  void createAttribute(Attribute attr, SchemaCreatorContext  context){
    println "createAttribute"
  }
  
  void createAttributeGroup(AttributeGroup attrG, SchemaCreatorContext  context){
    println "createAttributeGroup"
  }

}
