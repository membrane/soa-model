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

class AttributeDiffGenerator  extends AbstractDiffGenerator {
	
  private def labelTypeChanged, labelRefChanged, labelTo, labelAttributeChange, labelAttributeFormChange, labelAttributeFixedChange, labelAttributeDefaultChange
	
  def compare(){
    def diffs = compareType()
    diffs.addAll(compareUse())
    diffs.addAll(compareFixed())
    diffs
  }

  private compareType() {
    if(a.type != b.type) {
      return [new Difference(description:"{$labelTypeChanged} ${a.type} ${labelTo} ${b.type}.", type: 'attribute', breaks:true)]
    }
    []
  }

  private compareRef() {
    if(a.ref != b.ref) {
      return [new Difference(description:"${labelRefChanged} ${a.ref} ${labelTo} ${b.ref}.", type: 'attribute', breaks:true)]
    }
    []
  }

  private compareUse(){
    if(a.use != b.use) {
      return [new Difference(description:"${labelAttributeChange} ${a.use} ${labelTo} ${b.use}.", type: 'attribute')]
    }
    []
  }

  private compareForm(){
    if(a.form != b.form) {
      return [new Difference(description:"${labelAttributeFormChange} ${a.form} ${labelTo} ${b.form}.", type: 'attribute')]
    }
    []
  }

  private compareFixed(){
    if(a.fixedValue != b.fixedValue) {
      return [new Difference(description:"${labelAttributeFixedChange} ${a.fixedValue} ${labelTo} ${b.fixedValue}.", type: 'attribute')]
    }
    []
  }

  private compareDefault(){
    if(a.defaultValue != b.defaultValue) {
      return [new Difference(description:"${labelAttributeDefaultChange} ${a.defaultValue} ${labelTo} ${b.defaultValue}.", type: 'attribute')]
    }
    []
  }
	
  protected def updateLabels(){
	  
	   labelTypeChanged = bundle.getString("com.predic8.schema.diff.labelTypeChanged")
	   labelRefChanged = bundle.getString("com.predic8.schema.diff.labelRefChanged")
	   labelTo = bundle.getString("com.predic8.schema.diff.labelTo")
	   labelAttributeChange = bundle.getString("com.predic8.schema.diff.labelAttributeChange")
	   labelAttributeFormChange = bundle.getString("com.predic8.schema.diff.labelAttributeFormChange")
	   labelAttributeFixedChange = bundle.getString("com.predic8.schema.diff.labelAttributeFixedChange")
	   labelAttributeDefaultChange = bundle.getString("com.predic8.schema.diff.labelAttributeDefaultChange")

   }
  
}