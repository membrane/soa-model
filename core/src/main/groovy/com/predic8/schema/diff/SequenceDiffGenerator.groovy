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

  def generator

  def removed = {new Difference(description:"Sequence removed.", type: 'sequence', breaks: true, safe:false)}

  def added = { new Difference(description:"Sequence added.", type: 'sequence', breaks: true, safe:false)}

  def changed = { diffs ->
    new Difference(description:"Sequence has changed:" , type: 'sequence' ,  diffs : diffs, a: a, b:b, breaks: false, safe: true)
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
        diffs << new Difference(description:"Particle ${aP.elementName} replaced with ${bP.elementName}." , type: 'sequence')
        return
      }
      if(aP.name == bP.name) {
        bPs << bP
        def lDiffs = aP.compare(generator, bP)
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
      int bi = b.particles.findIndexOf{ it.name == aP.name }
      
      bPs << getParticleB(aP)
      if(aP instanceof Element) return new Difference(description:"Position of element ${aP.name} changed from $i to $bi." , type: 'sequence', safe: false, breaks: true)
      // "any" is not an element, so a different message here
      return new Difference(description:"Position of ${aP.elementName} changed from $i to $bi." , type: 'sequence', safe: false, breaks: true)
    }
    if(aP instanceof Element) return new Difference(description:"Element ${aP.name} removed." , type: 'sequence', safe: false, breaks: true)
    // "any" is not an element, so a different message here
    new Difference(description:"${aP.elementName} removed." , type: 'sequence', safe: false, breaks: true)
  }

  def compareUnprocessedBPs(bPs){
    def diffs = []
    (b.particles-bPs).eachWithIndex() { bP, i ->
        if(a.elements.find{it.name == bP.name}) {
        diffs << new Difference(description:"Position of element ${bP.name} changed." , type: 'sequence', breaks: true, safe: false)
      } else {
				diffs << new Difference(description:"${(bP.elementName).capitalize()} ${bP.name ? bP.name+' ' : ''}added with minOccurs=${bP.minOccurs}." , type: 'sequence', breaks: bP.minOccurs > '0', safe: bP.minOccurs == '0')
      }
    }
    diffs
  }

  private getParticleB(aP) {
    b.particles.find{it.name == aP.name}
  }
}