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

class AbstractModelDiffGenerator extends UnitDiffGenerator {

  def generator

  List<Difference> compareUnit(){
    def diffs = new ElementsDiffGenerator(a: a.elements, b: b.elements, generator: generator).compare()
    def aPs = (a.particles-a.elements)
    def bPs = (b.particles-b.elements)
    aPs.each{ aP ->
      bPs.each{ bP ->
        if(bP.class != aP.class) return
        if(!(aP.compare(generator, bP))){
          aPs -= aP
          bPs -= bP
        }
      }
    }
    diffs.addAll(writeRemovedParticles(aPs))
    diffs.addAll(wriTeAddedParticles(bPs))
    diffs
  }
  
  def writeRemovedParticles(aPs){
    def diffs = []
    aPs.each{
      diffs << new Difference(description:"Particle ${it.elementName} removed." , type: 'choice')
    }
    diffs
  }

  def wriTeAddedParticles(bPs){
    def diffs = []
    bPs.each{
      diffs << new Difference(description:"Particle ${it.elementName} added." , type: 'choice')
    }
    diffs
  }
}

