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

class SimpleTypeDiffGenerator extends UnitDiffGenerator {
	
	public SimpleTypeDiffGenerator() {
		updateLabels()
	}
	
  private def labelSimpleType, labelRemoved, labelAdded, labelHasChanged, labelList
	
  def removed = {new Difference(description:"${labelSimpleType} ${a?.name} ${labelRemoved}.", type: 'simpleType', breaks: ctx.exchange ? true: null, exchange: a.exchange)}
  def added = { new Difference(description:"${labelSimpleType} ${a?.name} ${labelAdded}.", type: 'simpleType', breaks: ctx.exchange ? true: null, exchange: b.exchange)}
  def changed = { new Difference(description:"${labelSimpleType} ${"{" + a.schema.targetNamespace + "}"}${a?.name}:", type: 'simpleType', diffs:compareUnit(), exchange: a.exchange)}

  List<Difference> compareUnit(){
    def lDiffs = []
    lDiffs.addAll(generator.compareAnnotation(a.annotation, b.annotation))
    if(a.restriction && b.restriction) {
      lDiffs.addAll(a.restriction.compare(generator, b.restriction))
    }
    if(a.list?.itemType != b.list?.itemType) {
      lDiffs.add(new Difference(description:"${labelList} ${labelHasChanged}.", type: 'simpleType', diffs:compareUnit(), exchange: a.exchange))
    }
    // Union is not handled yet.
    lDiffs
  }
  
  protected def updateLabels(){
	  labelSimpleType = bundle.getString("com.predic8.schema.diff.labelSimpleType")
	  labelRemoved = bundle.getString("com.predic8.schema.diff.labelRemoved")
	  labelAdded = bundle.getString("com.predic8.schema.diff.labelAdded")
	  labelHasChanged = bundle.getString("com.predic8.schema.diff.labelHasChanged")
	  labelList = bundle.getString("com.predic8.schema.diff.labelList")

  }
}

