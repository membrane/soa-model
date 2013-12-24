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

  def removed = { new Difference(description:"${labelElement} ${it.name ?: 'ref to ' + it.refValue} with minOccurs ${it?.minOccurs} ${labelRemoved}." ,
		 type : 'element', breaks:ctx.exchange ? true: null, exchange: it.exchange) }

  def added = { new Difference(description:"${labelElement} ${it.name ?: 'ref to ' + it.refValue} with minOccurs ${it?.minOccurs} ${labelAdded}.",
		 type : 'element', breaks:ctx.exchange ? true: null, exchange: it.exchange)}
  

  public ElementsDiffGenerator(){
	  updateLabels()
  }
	
  protected getIntersection(){
		def intersection = []
		a.each {aP ->
			b.each {bP ->
				if((aP.name ?: aP.ref) == (bP.name ?: bP.ref)) intersection << (aP.name ?: aP.ref)
			}
		}
		intersection
  }

  List<Difference> compareUnit(identificator){
    def aElement = a.find{ it.name == identificator} ?: a.find{it.ref == identificator}
    def bElement = b.find{ it.name == identificator} ?: b.find{it.ref == identificator}
    aElement.compare(generator , bElement, ctx.clone())
  }
  
  protected def updateLabels(){
	  labelElement = bundle.getString("com.predic8.schema.diff.labelElement")
	  labelRemoved = bundle.getString("com.predic8.schema.diff.labelRemoved")
	  labelAdded = bundle.getString("com.predic8.schema.diff.labelAdded")

  }
}
