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
import com.predic8.schema.*

class ChoiceDiffGenerator extends AbstractModelDiffGenerator{
  
  private def labelChoice, labelRemoved, labelAdded, labelHasChanged
  	
  def removed = {new Difference(description:"'${labelChoice}' ${labelRemoved}.", type: 'choice', breaks: true, safe:false)}

  def added = { new Difference(description:"'${labelChoice}' ${labelAdded}.", type: 'choice', breaks: true, safe:false)}

  def changed = { diffs ->
    new Difference(description:"'${labelChoice}' ${labelHasChanged}:" , type: 'choice' ,  diffs : diffs, a: a, b:b)
  }
  
  public ChoiceDiffGenerator(){
	  updateLabels()
  }
  
  protected def updateLabels(){
	  labelChoice = bundle.getString("com.predic8.schema.diff.labelChoice")
	  labelRemoved = bundle.getString("com.predic8.schema.diff.labelRemoved")
	  labelAdded = bundle.getString("com.predic8.schema.diff.labelAdded")
	  labelHasChanged = bundle.getString("com.predic8.schema.diff.labelHasChanged")

  }
  
}

