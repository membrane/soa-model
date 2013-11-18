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
  			  labelPositionElement, labelChanged, labelElement, labelRemoved, labelAdded

  def removed = {new Difference(description:"${labelSequenceRemoved}.", type: 'sequence', breaks: true, safe:false, exchange: a.exchange)}

  def added = { new Difference(description:"${labelSequenceAdded}.", type: 'sequence', breaks: true, safe:false, exchange: b.exchange)}

  def changed = { diffs ->
    new Difference(description:"${labelSequenceChanged}:" , type: 'sequence' ,  diffs : diffs, exchange: a.exchange)
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
        diffs << particleChangedOrRemoved(bPs, aP, i)
        return
      }
      if(!(aP instanceof Element || bP instanceof Element) && aP.class != bP.class) {
        bPs << bP
        diffs << new Difference(description:"${labelParticle} ${aP.elementName} ${labelReplacedWith} ${bP.elementName}." , type: 'sequence', exchange: a.exchange)
        return
      }
      if(aP.identification == bP.identification) {
        bPs << bP
				aP.exchange.addAll(a.exchange)
				bP.exchange.addAll(b.exchange)
        def lDiffs = aP.compare(generator, bP, ctx.clone())
        diffs.addAll(lDiffs)
        return
      }
      diffs << particleChangedOrRemoved(bPs, aP, i)
      return
    }
    diffs.addAll(compareUnprocessedBPs(bPs))
    diffs
  }

  private particleChangedOrRemoved(bPs, aP, i){
    if(getParticleB(aP)) {
      int bi = b.particles.findIndexOf{ it.identification == aP.identification }
      
      bPs << getParticleB(aP)
      if(aP instanceof Element) return new Difference(description:"${labelPositionElement} ${aP.identification} changed from ${i+1} to ${bi+1}." , type: 'sequence', safe: false, breaks: true)
      // "any" is not an element, so a different message here
      return new Difference(description:"${labelPositionElement} ${aP.identification ?: aP.elementName} ${labelChanged}." , type: 'sequence', safe: false, breaks: true)
    }
    if(aP instanceof Element) return new Difference(description:"${labelElement} ${aP.identification} ${labelRemoved}." , type: 'sequence', safe: false, breaks: true)
    // "any" is not an element, so a different message here
    new Difference(description:"${aP.elementName} ${labelRemoved}." , type: 'sequence', safe: false, breaks: true)
  }

  def compareUnprocessedBPs(bPs){
    def diffs = []
    (b.particles-bPs).eachWithIndex() { bP, i ->
        if(a.elements.find{it.identification == bP.identification}) {
        diffs << new Difference(description:"${labelPositionElement} ${bP.identification ? bP.identification +' ' : ''}${labelChanged}." , type: 'sequence', breaks: true, safe: false)
      } else {
				boolean isSafe = (bP.minOccurs == '0' && !(b.exchange.contains('response') || b.exchange.contains('fault')))
				diffs << new Difference(description:"${(bP.elementName).capitalize()} ${bP.identification ? bP.identification+' ' : ''}${bP.elementName == 'element'? 'with minoccurs ' + bP?.minOccurs+' ' : ''}${labelAdded}." , type: 'sequence', breaks: !isSafe, safe: isSafe, exchange: b.exchange)
      }
    }
    diffs
  }

  private getParticleB(aP) {
		if(aP.elementName == 'any'){
			return b.particles.find{it.elementName == 'any' && it.namespace == aP.namespace}
		}
    b.particles.find{it.identification == aP.identification}
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
	  labelAdded = bundle.getString("com.predic8.schema.diff.labelAdded")

  }
}