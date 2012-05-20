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

class SimpleTypeDiffGenerator extends UnitDiffGenerator {

  def removed = {new Difference(description:"SimpleType removed.", type: 'simpleType', breaks: true, safe:false)}
  def added = { new Difference(description:"SimpleType added.", type: 'simpleType', breaks: true, safe:false)}
  def changed = { new Difference(description:"SimpleType has changed.", type: 'simpleType')}

  List<Difference> compareUnit(){
    def lDiffs = []
    //compare restriction, union and list
    lDiffs.addAll(generator.compareAnnotation(a.annotation, b.annotation))
    lDiffs
  }
}

