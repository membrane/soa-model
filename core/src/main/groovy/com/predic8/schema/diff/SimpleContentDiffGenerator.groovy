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
    if(a.restriction  && b.extension) {
      return [new Difference(description:"SimpleContent changed from 'restriction' to 'extension'", type: 'simpleContent', breaks:true)]
    }
    if(a.extension && b.restriction) {
      return [new Difference(description:"SimpleContent changed from 'extension' to 'restriction'", type: 'simpleContent', breaks:true)]
    }
    if(compareChild()){
      return [new Difference(description:"SimpleContent has changed: " , type: 'simpleContent', diffs: compareChild())]
    }
    []
  }

  private compareChild(){
    if(a.extension?.base != b.extension?.base){
      return [new Difference(description:"Extension base has changed from ${a.extension.base} to ${b.extension.base}." , type: 'extension', breaks:true)]
    }
    
    if(a.extension && b.extension){
      if(a.extension.model?.class != b.extension.model?.class){
        return [new Difference(description:"ModelGroup has changed from ${a.extension.model.class} to ${b.extension.model.class}." , type: 'extension', breaks:true)]
      } 
      return a.extension.model?.compare(generator, b.extension.model )
    }
    
//    if(a.restriction?.base != b.restriction?.base){
//      return [new Difference(description:"Restriction base has changed from ${a.restriction.base} to ${b.restriction.base}." , type: 'restriction', breaks:true)]
//    }
    
    if(a.restriction && b.restriction){
      return a.restriction.compare(generator, b.restriction) ?: []
    }
    
  }
}

