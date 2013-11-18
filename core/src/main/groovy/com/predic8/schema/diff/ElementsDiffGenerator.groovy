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

class ElementsDiffGenerator extends ListDiffGenerator{
 
   def labelElement, labelRemoved, labelAdded

  def generator

  def removed = { new Difference(description:"${labelElement} ${it.identification} ${labelRemoved}." , type : 'element', breaks:true, exchange: it.exchange) }

  def added = { new Difference(description:"${labelElement} ${it.identification} ${labelAdded}.", type : 'element', exchange: it.exchange)}
  

  public ElementsDiffGenerator(){
	  updateLabels()
  }
  
  protected getIntersection(){
    (a.identification).intersect(b.identification)
  }

  List<Difference> compareUnit(identification){
    def aElement = a.find{ it.identification == identification}
    def bElement = b.find{ it.identification == identification}
    aElement.compare(generator , bElement)
  }
  
  protected def updateLabels(){
	  labelElement = bundle.getString("com.predic8.schema.diff.labelElement")
	  labelRemoved = bundle.getString("com.predic8.schema.diff.labelRemoved")
	  labelAdded = bundle.getString("com.predic8.schema.diff.labelAdded")

  }
}
