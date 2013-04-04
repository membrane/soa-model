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
import groovy.xml.QName

class ComplexTypesDiffGenerator extends ListDiffGenerator{

   def labelRemoved, labelAdded, labelComplexType
	
  def removed = { new Difference(description:"${labelComplexType} ${it.qname.localPart.toString()} ${labelRemoved}.", type: 'complexType', breaks:true)}

  def added = {new Difference(description:"${labelComplexType} ${it.qname.localPart.toString()} ${labelAdded}.", type: 'complexType')}

  List<Difference> compareUnit(qname){
    findA(qname).compare(generator, findB(qname))
  }
  
  protected getIntersection(){
    (a.qname).intersect(b.qname)
  }
  
  protected def updateLabels(){
	  labelRemoved = bundle.getString("com.predic8.schema.diff.labelRemoved")
	  labelAdded = bundle.getString("com.predic8.schema.diff.labelAdded")
	  labelComplexType = bundle.getString("com.predic8.schema.diff.labelComplexType")

  }

}

