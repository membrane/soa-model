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

class AttributeDiffGenerator  extends AbstractDiffGenerator {

  def compare(){
    def diffs = compareType()
    diffs.addAll(compareUse())
    diffs.addAll(compareFixed())
    diffs
  }

  private compareType() {
    if(a.type != b.type) {
      return [new Difference(description:"The type has changed from ${a.type} to ${b.type}.", type: 'attribute', breaks:true)]
    }
    []
  }

  private compareRef() {
    if(a.ref != b.ref) {
      return [new Difference(description:"The ref has changed from ${a.ref} to ${b.ref}.", type: 'attribute', breaks:true)]
    }
    []
  }

  private compareUse(){
    if(a.use != b.use) {
      return [new Difference(description:"The attribute use has changed from ${a.use} to ${b.use}.", type: 'attribute')]
    }
    []
  }

  private compareForm(){
    if(a.form != b.form) {
      return [new Difference(description:"The attribute form has changed from ${a.form} to ${b.form}.", type: 'attribute')]
    }
    []
  }

  private compareFixed(){
    if(a.fixed != b.fixed) {
      return [new Difference(description:"The attribute fixed changed from ${a.fixed} to ${b.fixed}.", type: 'attribute')]
    }
    []
  }

  private compareDefault(){
    if(a.defaultValue != b.defaultValue) {
      return [new Difference(description:"The attribute default changed from ${a.defaultValue} to ${b.defaultValue}.", type: 'attribute')]
    }
    []
  }

}