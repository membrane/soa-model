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
	
  def removed = {new Difference(description:"group removed.", type: 'group')}

  def added = {new Difference(description:"group added.", type: 'group')}

  def changed = {new Difference(description:"group changed.", type: 'group')}

  List<Difference> compareUnit(){
    if(a.ref == b.ref) return []
    [new Difference(description:"Ref of group has changed:" , type: 'group')]
  }
}

