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

class SequenceDiffGenerator  extends UnitDiffGenerator {
	
  def labelSequenceRemoved, labelSequenceAdded, labelSequenceChanged, labelParticle, labelReplacedWith, 
  			  labelPositionElement, labelChanged, labelElement, labelRemoved, labelAddedMinOccurs

  def generator

  def removed = {new Difference(description:"${labelSequenceRemoved}.", type: 'sequence', breaks: true, safe:false)}

  def added = { new Difference(description:"${labelSequenceAdded}.", type: 'sequence', breaks: true, safe:false)}

  def changed = { diffs ->
    new Difference(description:"${labelSequenceChanged}:" , type: 'sequence' ,  diffs : diffs, a: a, b:b, breaks: false, safe: true)
  }

  
  public SequenceDiffGenerator(){
	  updateLabels()
  }
  
  List<Difference> compareUnit(){
    def diffs = []
    def bPs = []
    a.particles.eachWithIndex() { aP, i ->
      def bP = b.particles[i]
      if(!bP){
        diffs << elementChangedOrRemoved(bPs, aP)
        return
      }
      if(!(aP instanceof Element || bP instanceof Element) && aP.class != bP.class) {
        bPs << bP
        diffs << new Difference(description:"${labelParticle} ${aP.elementName} ${labelReplacedWith} ${bP.elementName}." , type: 'sequence')
        return
      }
      if(aP.name == bP.name) {
        bPs << bP
        def lDiffs = aP.compare(generator, bP)
        diffs.addAll(lDiffs)
        return
      }
      diffs << elementChangedOrRemoved(bPs, aP)
      return
    }
    diffs.addAll(compareUnprocessedBPs(bPs))
    diffs
  }

  private elementChangedOrRemoved(bPs, aP){
    if(getElementB(aP)) {
      bPs << getElementB(aP)
      return new Difference(description:"${labelPositionElement} ${aP.name} ${labelChanged}." , type: 'sequence', safe: false, breaks: true)
    }
    if(aP instanceof Element) return new Difference(description:"${labelElement} ${aP.name} ${labelRemoved}." , type: 'sequence', safe: false, breaks: true)
    new Difference(description:"${aP.elementName} ${labelRemoved}." , type: 'sequence', safe: false, breaks: true)
  }

  def compareUnprocessedBPs(bPs){
    def diffs = []
    (b.particles-bPs).eachWithIndex() { bP, i ->
        if(a.elements.find{it.name == bP.name}) {
        diffs << new Difference(description:"${labelPositionElement} ${bP.name} ${labelChanged}." , type: 'sequence', breaks: true, safe: false)
      } else {
				diffs << new Difference(description:"${(bP.elementName).capitalize()} ${bP.name ? bP.name+' ' : ''}${labelAddedMinOccurs}=${bP.minOccurs}." , type: 'sequence', breaks: bP.minOccurs > '0', safe: bP.minOccurs == '0')
      }
    }
    diffs
  }

  private getElementB(aP){
    b.elements.find{it.name == aP.name}
  }
  
  protected def updateLabels(){
	  labelSequenceRemoved = bundle.getString("com.predic8.schema.diff.labelSequenceRemoved")
	  labelSequenceAdded = bundle.getString("com.predic8.schema.diff.labelSequenceAdded")
	  labelSequenceChanged = bundle.getString("com.predic8.schema.diff.labelSequenceChanged")
	  labelParticle = bundle.getString("com.predic8.schema.diff.labelParticle")
	  labelReplacedWith = bundle.getString("com.predic8.schema.diff.labelReplacedWith")
	  labelPositionElement = bundle.getString("com.predic8.schema.diff.labelPositionElement")
	  labelChanged = bundle.getString("com.predic8.schema.diff.labelChanged")
	  labelElement = bundle.getString("com.predic8.schema.diff.labelElement")
	  labelRemoved = bundle.getString("com.predic8.schema.diff.labelRemoved")
	  labelAddedMinOccurs = bundle.getString("com.predic8.schema.diff.labelAddedMinOccurs")

  }
}