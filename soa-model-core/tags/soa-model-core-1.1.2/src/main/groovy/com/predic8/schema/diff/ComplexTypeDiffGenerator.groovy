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

class ComplexTypeDiffGenerator extends UnitDiffGenerator{

  def removed = {new Difference(description:"ComplexType removed.", type: 'complexType', breaks: true, safe:false)}

  def added = { new Difference(description:"ComplexType added.", type: 'complexType', breaks: true, safe:false)}

  def changed = { diffs ->
    new Difference(description:"ComplexType ${a.qname?.localPart ?: ''} has changed:" , type: 'complexType' ,  diffs : diffs, a: a, b:b)
  }

  List<Difference> compareUnit(){
    def lDiffs = new AnnotationDiffGenerator(a: a.annotation, b: b.annotation, generator: generator).compare()
    lDiffs.addAll( compareModel())
    lDiffs
  }
  
  private compareModel(){
    def aType = a
    def bType = b
    def lDiffs
    if(aType.model?.class != bType.model?.class){
      lDiffs = new Difference(description:"ModelGroup has changed from ${aType.model?.class} to ${bType.model?.class}." , type: 'model', breaks:true)
    } else {
      lDiffs = aType.model?.compare(generator, bType.model ) ?: []
    }
    
    lDiffs.addAll(generator.compareAttributes(aType, bType))
//    compareAttributeGroups not implemented yet!
//    lDiffs.addAll(generator.compareAttributeGroups(aType, bType))
    lDiffs.addAll(generator.compareAnnotation(aType.annotation, bType.annotation))
    lDiffs
  }
}

