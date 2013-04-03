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

class SimpleTypesDiffGenerator extends ListDiffGenerator {

   def labelRemoved, labelAdded, labelSimpleType	
	
  def generator

  def removed = { new Difference(description:"${labelSimpleType} ${it.qname.toString()} ${labelRemoved}.", type: 'simpleType', breaks:true)}

  def added = {new Difference(description:"${labelSimpleType} ${it.qname.toString()} ${labelAdded}.", type: 'simpleType')}

  protected getIntersection(){
    (a.qname).intersect(b.qname)
  }

  List<Difference> compareUnit(qname){
    findA(qname).compare(generator, findB(qname))
  }
  
  protected def updateLabels(){
	  labelSimpleType = bundle.getString("com.predic8.schema.diff.labelSimpleType")
	  labelRemoved = bundle.getString("com.predic8.schema.diff.labelRemoved")
	  labelAdded = bundle.getString("com.predic8.schema.diff.labelAdded")

  }
}

