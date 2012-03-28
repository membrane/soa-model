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
    new Difference(description:"Sequence has changed:" , type: 'sequence' ,  diffs : diffs, a: a, b:b)
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
        diffs << new Difference(description:"Particle ${aP.elementName} replaced with ${bP.elementName}." , type: 'sequence')
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
      return new Difference(description:"Position of element ${aP.name} changed." , type: 'sequence')
    }
    if(aP instanceof Element) return new Difference(description:"Element ${aP.name} removed." , type: 'sequence')
    new Difference(description:"${aP.elementName} removed." , type: 'sequence')
  }

  def compareUnprocessedBPs(bPs){
    def diffs = []
    (b.particles-bPs).eachWithIndex() { bP, i ->
      if(a.elements.find{it.name == bP.name}) {
        diffs << new Difference(description:"Position of element ${bP.name} changed." , type: 'sequence')
      } else{
        if(bP instanceof Element) diffs << new Difference(description:"Element ${bP.name} added." , type: 'sequence')
        else diffs << new Difference(description:"${bP.elementName} added." , type: 'sequence')
      }
    }
    diffs
  }

  private getElementB(aP){
    b.elements.find{it.name == aP.name}
  }
}