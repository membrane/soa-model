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

import java.util.ResourceBundle;

import com.predic8.soamodel.*
import com.predic8.schema.*

class AllDiffGenerator extends AbstractModelDiffGenerator{
  
//  protected ResourceBundle bundle = ResourceBundle.getBundle("LabelsBundle", new Locale("en", "US"))
  private def labelHasChanged, labelRemoved, labelAdded

  def removed = {new Difference(description:"'All' ${labelRemoved}.", type: 'all', breaks: true, safe:false)}

  def added = { new Difference(description:"'All' ${labelAdded}.", type: 'all', breaks: true, safe:false)}
  
  
  def changed = { diffs ->
    new Difference(description:"'All' ${labelHasChanged}:" , type: 'all' ,  diffs : diffs, a: a, b:b)
  }
  
  public AllDiffGenerator(){
	  updateLabels()
  }
  
  protected def updateLabels(){
	  
	  labelHasChanged = bundle.getString("com.predic8.schema.diff.labelHasChanged")
	  labelRemoved = bundle.getString("com.predic8.schema.diff.labelRemoved")
	  labelAdded = bundle.getString("com.predic8.schema.diff.labelAdded")
  }
 
}

