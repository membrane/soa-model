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

package com.predic8.schema.diff

import java.util.List;
import com.predic8.soamodel.*
import com.predic8.schema.*
import org.apache.commons.logging.*

class SchemaDiffGenerator extends AbstractDiffGenerator{

  private Log log = LogFactory.getLog(this.class)
  
//  To avoid cycling schema imports. Otherwise it would cause a StackOverFlow exception.  
  private List<String> alreadyImportedNamespaces = [] 
  
  public SchemaDiffGenerator(){}
  
  public SchemaDiffGenerator(Schema a, Schema b){
    this.a = a
    this.b = b
  }

  List<Difference> compare(){
    def diffs = []
    log.debug("compare schema")
    
    if ( a.targetNamespace != b.targetNamespace ){
      return diffs << new Difference(description:"targetNamespace changed from ${a.targetNamespace} to ${b.targetNamespace}", type: 'targetNamespace', breaks:true)
    }
    if ( alreadyImportedNamespaces?.contains(a.targetNamespace)){
      return diffs
    }
    diffs.addAll(compareAnnotation(a?.annotation, b?.annotation))
    diffs.addAll(compareComplexTypes())
    diffs.addAll(compareSimpleTypes())
    diffs.addAll(compareElements())
    diffs.addAll(compareImports())
    
    diffs
  }
  
  private def compareComplexTypes(){
    new ComplexTypesDiffGenerator(a: a.complexTypes, b: b.complexTypes, generator: this).compare()
  }

  private def compareComplexType(a, b){
    new ComplexTypeDiffGenerator(a: a, b: b, generator: this).compare()
  }

  def compareSequence(a, b) {
    new SequenceDiffGenerator(a: a, b: b, generator: this).compare()
  }

  def compareChoice(a, b) {
    new ChoiceDiffGenerator(a: a, b: b, generator: this).compare()
  }
  
  def compareAll(a, b) {
    new AllDiffGenerator(a: a, b: b, generator: this).compare()
  }

  def compareGroups(groupA, groupB ) {
    throw new RuntimeException('compareGroups() not implemented yet!')
  }
  
  def compareGroupRef( groupRefA, groupRefB ) {
    new GroupRefDiffGenerator(a: groupRefA, b: groupRefB, generator: this).compare()
  }
  
  private def compareSimpleTypes(){
    new SimpleTypesDiffGenerator(a: a.simpleTypes, b: b.simpleTypes, generator: this).compare()
  }

  private def compareSimpleType(a, b){
    new SimpleTypeDiffGenerator(a: a, b: b, generator: this).compare()
  }

  private def compareElements(){
    new ElementsDiffGenerator(a: a?.elements ?: [], b: b?.elements ?: [], generator: this).compare()
  }

  def compareElement(a, b){
    new ElementDiffGenerator(a: a, b: b, generator: this).compare()
  }
  
  private def compareImports(){
    alreadyImportedNamespaces?.add(a.targetNamespace)
    new ImportsDiffGenerator(a: a.imports, b: b.imports, generator: this, alreadyImportedNamespaces: alreadyImportedNamespaces).compare()
  }
  
  def compareAttributes(a,b){
    new AttributesDiffGenerator(a: a.attributes, b: b.attributes, generator: this).compare()
  }
  
  def compareAttribute(a,b){
    new AttributeDiffGenerator(a: a, b: b, generator: this).compare()
  }

  def compareAnnotation(a, b){
    new AnnotationDiffGenerator(a: a, b: b, generator: this).compare()
  }
  
  def compareComplexContent(a, b){
    new ComplexContentDiffGenerator(a: a, b: b, generator: this).compare()
  }
  
  def compareSimpleContent(a, b) {
    new SimpleContentDiffGenerator(a: a, b: b, generator: this).compare()
  }

  def compareSimpleRestriction(a, b){
    new SimpleRestrictionDiffGenerator(a: a, b: b, generator: this).compare()
  }
}
