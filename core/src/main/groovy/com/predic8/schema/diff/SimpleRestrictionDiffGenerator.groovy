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

import java.util.List;

import com.predic8.soamodel.*
import com.predic8.schema.*

class SimpleRestrictionDiffGenerator extends AbstractDiffGenerator{
	
	public SimpleRestrictionDiffGenerator() {
		updateLabels()
	}

  List<Difference> compare(){
    def diffs = compareBase()
    diffs << compareFacets()

    diffs.flatten()
   
  }

  List<Difference> compareBase(){
    if(a.base != b.base) {
      return [new Difference(description:"Restriction base has changed from ${a.base} to ${b.base}." , type: 'restriction', warning:ctx.exchange ? true: null)]
    }
    []
  }

  List<Difference> compareFacets(){
    def diffs = []
    if ( a.facets.isEmpty() && b.facets.isEmpty() ) return []
    diffs << compare(a.enumerationFacets, b.enumerationFacets, {new Difference(description:"Enumeration with value: ${it.value} removed.", type: 'facet', warning: true)}, {new Difference(description:"Enumeration with value: ${it.value} added.", type: 'facet', warning: true)})
    
//    def aNotEnums = a.facets - a.enumerationFacets
    def aNotEnums = a.facets.findAll { f -> !a.enumerationFacets.find { ef -> f == ef } }
//    def bNotEnums = b.facets - b.enumerationFacets
    def bNotEnums = b.facets.findAll { f -> !b.enumerationFacets.find { ef -> f == ef } }
    diffs << compareNotEnumFacets(aNotEnums.elementName, bNotEnums.elementName)
    diffs
  }
  
  List<Difference> compareNotEnumFacets(aFs, bFs){
    def diffs = []
//    def removed  = aFs - bFs
    def removed = aFs.findAll { af -> !bFs.find { af == it } }
//    def added = bFs - aFs
    def added = bFs.findAll { bf -> !aFs.find { bf == it } }

    removed.each{
      diffs << new Difference(description:"Facet $it removed.", type: 'facet', warning:ctx.exchange ? true: null)
    }
    added.each{
      diffs << new Difference(description:"Facet $it added.", type: 'facet', warning:ctx.exchange ? true: null)
    }
    (aFs).intersect(bFs).each { fName ->
      def aFV = a.facets.find{it.elementName == fName}.value
      def bFV = b.facets.find{it.elementName == fName}.value
      if(aFV != bFV) diffs << new Difference(description:"Value of $fName changed from ${aFV} to ${bFV}.", type: 'facet', warning: true)
    }
    diffs.flatten()
  }
  
  protected def updateLabels(){
  }
  
}

