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

class SimpleContentDiffGenerator extends AbstractDiffGenerator{

  def generator

  def compare(){
    compareDerivation()
  }

  private compareDerivation(){
    if(a.restriction  && b.extension) {
      return [new Difference(description:"SimpleContent changed from 'restriction' to 'extension'", type: 'simpleContent', breaks:true)]
    }
    if(a.extension && b.restriction) {
      return [new Difference(description:"SimpleContent changed from 'extension' to 'restriction'", type: 'simpleContent', breaks:true)]
    }
    if(compareModel()){
      return [new Difference(description:"SimpleContent has changed: " , type: 'simpleContent', diffs: compareModel())]
    }
    []
  }

  private compareModel(){
    def a = a.extension ?: a.restriction
    def b = b.extension ?: b.restriction
    if(a.base != b.base){
      return [new Difference(description:"Extension base has changed from ${a.base} to ${b.base}." , type: 'extension', breaks:true)]
    } 
  }
}

