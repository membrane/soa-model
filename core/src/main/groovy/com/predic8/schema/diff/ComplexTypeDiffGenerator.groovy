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

class ComplexTypeDiffGenerator extends UnitDiffGenerator{
	
  public ComplexTypeDiffGenerator() {
	  updateLabels()
  }

	private def labelModelGroupChange, labelHasChanged, labelTo, labelRemoved, labelAdded, labelComplexType
  
  def removed = {new Difference(description:"${labelComplexType} ${labelRemoved}.", type: 'complexType', breaks: ctx.exchange ? true: null, exchange: a.exchange)}

  def added = { new Difference(description:"${labelComplexType} ${labelAdded}.", type: 'complexType', breaks: ctx.exchange ? true: null, exchange: b.exchange)}

  def changed = { diffs ->
    new Difference(description:"${labelComplexType} ${a.qname?.localPart ? "{" + a.schema.targetNamespace + "}": ''}${a.qname?.localPart ?: ''}:" , type: 'complexType' ,  diffs : diffs, exchange: a.exchange)
  }

  List<Difference> compareUnit(){
		def lDiffs = new AnnotationDiffGenerator(a: a.annotation, b: b.annotation, generator: generator).compare()
    lDiffs.addAll( compareModel())
    lDiffs
  }
  
  private compareModel(){
    def lDiffs = []
    if(a.model && b.model && a.model?.class != b.model?.class){
      lDiffs << new Difference(description:"${labelModelGroupChange} ${a.model?.class?.simpleName} ${labelTo} ${b.model?.class?.simpleName}." , type: 'model', breaks:ctx.exchange ? true: null, exchange: a.exchange)
    } else if(a.model && b.model) {
      a.model.exchange = a.exchange
	  b.model.exchange = b.exchange
      lDiffs.addAll(a.model.compare(generator, b.model, ctx.clone()))
    } else if(a.model){ //b has no model
      lDiffs << new Difference(description:"${a.model.elementName} ${labelRemoved}." , type: 'model', breaks:true, exchange: a.exchange)
    } else if(b.model){ //a has no model
	  lDiffs << new Difference(description:"${b.model.elementName} ${labelAdded}." , type: 'model', breaks:true, exchange: b.exchange)
    }
    
    lDiffs.addAll(generator.compareAttributes(a, b))
		//TODO  compareAttributeGroups not implemented yet!
//    lDiffs.addAll(generator.compareAttributeGroups(a, b))
    lDiffs
  }
  
  protected def updateLabels(){
	  labelModelGroupChange = bundle.getString("com.predic8.schema.diff.labelModelGroupChange")
	  labelHasChanged = bundle.getString("com.predic8.schema.diff.labelHasChanged")
	  labelTo = bundle.getString("com.predic8.schema.diff.labelTo")
	  labelRemoved = bundle.getString("com.predic8.schema.diff.labelRemoved")
	  labelAdded = bundle.getString("com.predic8.schema.diff.labelAdded")
	  labelComplexType = bundle.getString("com.predic8.schema.diff.labelComplexType")

  }
}

