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
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SchemaDiffGenerator extends AbstractDiffGenerator{

  private static final Logger log = LoggerFactory.getLogger(SchemaDiffGenerator.class)

  def labelTN, labelTo
	
	/*
	 * If true, elements must compare not only their type name, but their real types.
	 */
	boolean compare4WSDL
    
//  To avoid cycling schema imports. Otherwise it would cause a StackOverFlow exception.  
  private List<String> alreadyImportedNamespaces = [] 
  
  public SchemaDiffGenerator(){
    updateLabels()
  }
  
	//ctx
  public SchemaDiffGenerator(Schema a, Schema b){
    this.a = a
    this.b = b
    updateLabels()
  }
  
  public SchemaDiffGenerator(Schema a, Schema b, Locale locale){
	  bundle = ResourceBundle.getBundle("LabelsBundle", locale)
	  this.a = a
	  this.b = b
	  updateLabels()
  }

  List<Difference> compare(){
    def diffs = []
    log.debug("compare schema")
    
    if ( a.targetNamespace != b.targetNamespace ){
      diffs << new Difference(description:"${labelTN} ${a.targetNamespace} ${labelTo} ${b.targetNamespace}", type: 'targetNamespace', breaks:ctx.exchange ? true: null)
    }
    if ( alreadyImportedNamespaces?.contains(a.targetNamespace)){
      return diffs
    }
    diffs.addAll(compareAnnotation(a?.annotation, b?.annotation))
    diffs.addAll(compareElements())
    diffs.addAll(compareComplexTypes())
    diffs.addAll(compareSimpleTypes())
    diffs.addAll(compareImports())
    
    diffs
  }
  
  private def compareComplexTypes(){
    new ComplexTypesDiffGenerator(a: a.complexTypes, b: b.complexTypes, generator: this).compare()
  }

  private def compareComplexType(a, b, ctx){
    new ComplexTypeDiffGenerator(a: a, b: b, generator: this, ctx: ctx).compare()
  }

  def compareSequence(a, b, ctx) {
    new SequenceDiffGenerator(a: a, b: b, generator: this, ctx: ctx).compare()
  }

  def compareChoice(a, b, ctx) {
    new ChoiceDiffGenerator(a: a, b: b, generator: this, ctx: ctx).compare()
  }
  
  def compareAll(a, b, ctx) {
    new AllDiffGenerator(a: a, b: b, generator: this, ctx: ctx).compare()
  }

  def compareGroups(groupA, groupB ) {
    throw new RuntimeException('compareGroups() not implemented yet!')
  }
  
  def compareGroupRef( groupRefA, groupRefB, ctx) {
    new GroupRefDiffGenerator(a: groupRefA, b: groupRefB, generator: this, ctx: ctx).compare()
  }
  
  private def compareSimpleTypes(){
    new SimpleTypesDiffGenerator(a: a.simpleTypes, b: b.simpleTypes, generator: this).compare()
  }

  private def compareSimpleType(a, b, ctx){
    new SimpleTypeDiffGenerator(a: a, b: b, generator: this, ctx: ctx).compare()
  }

  private def compareElements(){
    new ElementsDiffGenerator(a: a?.elements ?: [], b: b?.elements ?: [], generator: this).compare()
  }

  def compareElement(a, b, ctx){
    new ElementDiffGenerator(a: a, b: b, generator: this, ctx: ctx).compare()
  }
  
  private def compareImports(){
    alreadyImportedNamespaces?.add(a.targetNamespace)
    new ImportsDiffGenerator(a: a.imports, b: b.imports, generator: this, alreadyImportedNamespaces: alreadyImportedNamespaces).compare()
  }
  
  def compareAttributes(a,b){
    new AttributesDiffGenerator(a: a.attributes, b: b.attributes, generator: this).compare()
  }
  
  def compareAttribute(a,b, ctx){
    new AttributeDiffGenerator(a: a, b: b, generator: this, ctx: ctx).compare()
  }

  def compareAnnotation(a, b){
    new AnnotationDiffGenerator(a: a, b: b, generator: this).compare()
  }

  def compareAny(a, b, ctx) {
    new AnyDiffGenerator(a: a, b: b, generator: this, ctx: ctx).compare()
  }
  
  def compareComplexContent(a, b, ctx){
    new ComplexContentDiffGenerator(a: a, b: b, generator: this, ctx: ctx).compare()
  }
  
  def compareSimpleContent(a, b, ctx) {
    new SimpleContentDiffGenerator(a: a, b: b, generator: this, ctx: ctx).compare()
  }

  def compareSimpleRestriction(a, b, ctx){
    new SimpleRestrictionDiffGenerator(a: a, b: b, generator: this, ctx: ctx).compare()
  }
	
	def compareBuiltInSchemaType(a, b, ctx){
    new BuiltInSchemaTypeDiffGenerator(a: a, b: b, generator: this, ctx: ctx).compare()
  }
  
  protected def updateLabels(){
	  labelTN = bundle.getString("com.predic8.schema.diff.labelTN")
	  labelTo = bundle.getString("com.predic8.schema.diff.labelTo")
  }
}
