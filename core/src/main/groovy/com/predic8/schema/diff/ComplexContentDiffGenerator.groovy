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

import com.predic8.soamodel.*

class ComplexContentDiffGenerator extends AbstractDiffGenerator{
	
	public ComplexContentDiffGenerator(){
		updateLabels()
	}

  def generator

  private def labelContentModelElement, labelContentModelElementMixed, labelComplexContentChangeExtension, 
  			  labelComplexContentChangeRestriction, labelComplexContentChange, labelHasChanged, labelRemoved, labelAdded
  
  def compare(){
    def diffs = compareMixed()
    diffs.addAll(compareDerivation())
    diffs
  }

  private compareMixed(){
    if(a.mixed  && !b.mixed) return [new Difference(description:"${labelContentModelElement}", type: 'complexContent', breaks:ctx.exchange ? true: null, exchange: a.exchange)]
    if(!a.mixed  && b.mixed) return [new Difference(description:"${labelContentModelElementMixed}", type: 'complexContent', breaks:ctx.exchange ? true: null, exchange: a.exchange)]
    []
  }

  private compareDerivation(){
    if(a.hasRestriction()  && b.hasExtension()) {
      return [new Difference(description:"${labelComplexContentChangeExtension}", type: 'complexContent', breaks:ctx.exchange ? true: null, exchange: a.exchange)]
    }
    if(a.hasExtension() && b.hasRestriction()) {
      return [new Difference(description:"${labelComplexContentChangeRestriction}", type: 'complexContent', breaks:ctx.exchange ? true: null, exchange: a.exchange)]
		  
    }
		def lDiffs = compareModel() 
    if(lDiffs){
      return [new Difference(description:"${labelComplexContentChange}: " , type: 'complexContent', diffs: lDiffs, exchange: a.exchange)]
    }
    []
  }

  private compareModel(){
    def lDiffs
    
    // Check that a and b derivation ModelGroup aren't the same
    if(a.derivation.model && b.derivation.model
      && a.derivation.model.class != b.derivation.model.class) {
      lDiffs = [new Difference(description:"ModelGroup has changed from '${a.derivation.model.elementName}' to '${b.derivation.model.elementName}'.", type: 'model', breaks:true, exchange: a.exchange)]
    }
    else { // if one or both derivation model are not set or they are of same class
      
      def aDerivationModelOverride = a.derivation.model
      def bDerivationModelOverride = b.derivation.model
      
      // We instantiate empty derivation models with the same type as a or b
      if(a.derivation.model && !b.derivation.model) { // only a has a derivation model => derivation model removed
        bDerivationModelOverride = a.derivation.model.getClass().newInstance();
      }
      else if(b.derivation.model && !a.derivation.model) { // only b has a derivation model => derivation model added
        aDerivationModelOverride = b.derivation.model.getClass().newInstance();
      }
      
      lDiffs = aDerivationModelOverride?.compare(generator, bDerivationModelOverride, ctx.clone())
    }
    
    if(lDiffs && lDiffs.size() > 0) {
      return [new Difference(description:"${a.derivation.elementName.localPart.capitalize()}: " , type: 'complexContent',
        diffs: lDiffs, exchange: a.exchange)]
    }
		[]
  }
  
  protected def updateLabels(){
	  labelContentModelElement = bundle.getString("com.predic8.schema.diff.labelContentModelElement")
	  labelContentModelElementMixed = bundle.getString("com.predic8.schema.diff.labelContentModelElementMixed")
	  labelComplexContentChangeExtension = bundle.getString("com.predic8.schema.diff.labelComplexContentChangeExtension")
	  labelComplexContentChangeRestriction = bundle.getString("com.predic8.schema.diff.labelComplexContentChangeRestriction")
	  labelComplexContentChange = bundle.getString("com.predic8.schema.diff.labelComplexContentChange")
	  labelHasChanged = bundle.getString("com.predic8.schema.diff.labelHasChanged")
	  labelRemoved = bundle.getString("com.predic8.schema.diff.labelRemoved")
	  labelAdded = bundle.getString("com.predic8.schema.diff.labelAdded")
  }
}

