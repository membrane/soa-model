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

class GroupRefDiffGenerator extends UnitDiffGenerator{
	
  private def labelGroup, labelRemoved, labelAdded, labelChanged, labelHasChanged, labelRef
	
  def removed = {new Difference(description:"${labelGroup} ${labelRemoved}.", type: 'group')}

  def added = {new Difference(description:"${labelGroup} ${labelAdded}.", type: 'group')}

  def changed = {new Difference(description:"${labelGroup} ${labelChanged}.", type: 'group')}

  List<Difference> compareUnit(){
    if(a.ref == b.ref) return []
    [new Difference(description:"${labelRef} {labelGroup} ${labelHasChanged}:" , type: 'group')]
  }
  
  protected def updateLabels(){
	  labelGroup = bundle.getString("com.predic8.schema.diff.labelGroup")
	  labelRemoved = bundle.getString("com.predic8.schema.diff.labelRemoved")
	  labelAdded = bundle.getString("com.predic8.schema.diff.labelAdded")
	  labelChanged = bundle.getString("com.predic8.schema.diff.labelChanged")
	  labelHasChanged = bundle.getString("com.predic8.schema.diff.labelHasChanged")
	  labelRef = bundle.getString("com.predic8.schema.diff.labelRef")

  }
}

