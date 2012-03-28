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

class ComplexContentDiffGenerator extends AbstractDiffGenerator{

  def generator

  def compare(){
    def diffs = compareMixed()
    diffs.addAll(compareDerivation())

    diffs
  }

  private compareMixed(){
    if(a.mixed  && !b.mixed) return [new Difference(description:"Content model of complexContent changed from 'mixed' to 'element-only'", type: 'complexContent', breaks:true)]
    if(!a.mixed  && b.mixed) return [new Difference(description:"Content model of complexContent changed from 'element-only' to 'mixed'", type: 'complexContent', breaks:true)]
    []
  }

  private compareDerivation(){
    if(a.hasRestriction()  && b.hasExtension()) {
      return [new Difference(description:"ComplexContent changed from 'restriction' to 'extension'", type: 'complexContent', breaks:true)]
    }
    if(a.hasExtension() && b.hasRestriction()) {
      return [new Difference(description:"ComplexContent changed from 'extension' to 'restriction'", type: 'complexContent', breaks:true)]
    }
    if(compareModel()){
      return [new Difference(description:"ComplexContent has changed: " , type: 'complexContent', diffs: compareModel())]
    }
    []
  }

  private compareModel(){
    if(a.derivation.model?.class != b.derivation.model?.class){
      return [new Difference(description:"ModelGroup has changed from ${a.derivation.model.class} to ${b.derivation.model.class}." , type: 'model', breaks:true)]
    } 
    return a.derivation.model?.compare(generator, b.derivation.model )[]
  }
}

