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
      if(!bP){														//Index of aP does not exist in bPs 
				if(aP instanceof Element) {
					int bi = getParticleBIndex(aP)	//for aP there is a bP with another index
					if(bi != -1) {
						bPs << b.particles[bi]
						diffs << new Difference(description:"${labelPositionElement} ${aP.name} changed from ${i+1} to ${bi+1}." , type: 'sequence', safe: false, breaks: true)
						return
					}
					diffs << new Difference(description:"${labelElement} ${aP.name ?: 'ref to ' + aP.refValue} ${labelRemoved}." ,
						type: 'sequence', safe: false, breaks: true)
					return
				}
				if(aP instanceof Any && bPs.grep(Any).find{it.namespace == aP.namespace}) {
					diffs << new Difference(description:"${labelPositionElement} 'any' ${labelChanged}." , type: 'sequence', safe: false, breaks: true)
					bPs << bPs.grep(Any).find{it.namespace == aP.namespace}
					return
				}
				diffs << new Difference(description:"${aP.elementName} ${labelRemoved} from position ${i+1}." , type: 'sequence', safe: false, breaks: true)
        return
      }
			if(aP instanceof Element){														//aP is an element
				if(bP instanceof Element){													//bP is also an elements
					if((aP.name ?: aP.ref) == (bP.name ?: bP.ref)) {	//Element names or refs are equal
						bPs << bP
						aP.exchange.addAll(a.exchange)
						bP.exchange.addAll(b.exchange)
		        def lDiffs = aP.compare(generator, bP, ctx.clone())
		        diffs.addAll(lDiffs)
		        return
					}
					int bi = getParticleBIndex(aP)
					if(bi != -1) {													//element found on other position
						bPs << b.particles[bi]
						diffs << new Difference(description:"${labelPositionElement} ${aP.name} changed from ${i+1} to ${bi+1}." , type: 'sequence', safe: false, breaks: true)
						return
					}																
				}
				int bi = getParticleBIndex(aP)
				if(bi != -1) {														//element found on other position
					bPs << b.particles[bi]
					diffs << new Difference(description:"${labelPositionElement} ${aP.name} changed from ${i+1} to ${bi+1}." , type: 'sequence', safe: false, breaks: true)
					return
				}																					//element not found (removed) or bP is not an element
				diffs << new Difference(description:"${labelElement} ${aP.name?: 'ref to ' + aP.refValue} ${labelRemoved}." , type: 'sequence', safe: false, breaks: true)
				return
			}																						//aP is NOT an element
			if(aP instanceof Any) {											//aP is an any
				int bi = getParticleBIndex(aP)
				if(bi != -1 && bi != i) {									//any found on other position
					bPs << b.particles[bi]
					diffs << new Difference(description:"Position of any changed from ${i+1} to ${bi+1}." , type: 'sequence', safe: false, breaks: true)
					return
				}						
			}
			if(bP instanceof Element){									//aP is not an element and bP is an element
				diffs << new Difference(description:"${aP.elementName} ${labelRemoved}  from position ${i+1}." , type: 'sequence', safe: false, breaks: true)
				return
			}																						//compare two none-Elements (also 'any')
			if(aP.class == bP.class){
				bPs << bP
				aP.exchange.addAll(a.exchange)
				bP.exchange.addAll(b.exchange)
	      def lDiffs = aP.compare(generator, bP, ctx.clone())
	      diffs.addAll(lDiffs)
				return
			}
			bPs << bP
			diffs << new Difference(description:"${labelParticle} ${aP.elementName} on position ${i+1} ${labelReplacedWith} ${bP.elementName}." , type: 'sequence', exchange: a.exchange)
			return
    }
    diffs.addAll(compareUnprocessedBPs(b.particles - bPs))
    diffs
  }

  def compareUnprocessedBPs(bPs){
    def diffs = []
    bPs.each { bP ->
			boolean isSafe = (bP.minOccurs == '0' && !(b.exchange.contains('response') || b.exchange.contains('fault')))
			if(bP instanceof Element) {
				diffs << new Difference(description:"${labelElement} ${bP.name?: 'ref to ' + bP.refValue} with minoccurs ${bP?.minOccurs} ${labelAdded}." , type: 'sequence', breaks: !isSafe, safe: isSafe, exchange: b.exchange)
			} else {
				diffs << new Difference(description:"New ${bP.elementName} ${labelAdded} to position ${b.particles.findIndexOf{it == bP}+1}." , type: 'sequence', breaks: !isSafe, safe: isSafe, exchange: b.exchange)
			}
		}
    diffs
  }
	
	private getParticleBIndex(aP) {
		if(aP instanceof Element) return	b.particles.findIndexOf{it instanceof Element && (it.name ?: it.ref) == (aP.name ?: aP.ref)}
		if(aP instanceof Any) return b.particles.findIndexOf{it instanceof Any && it.namespace == aP.namespace}
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