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

  def compare(){
    def diffs = compareBase()
    diffs.addAll(compareFacets())

    diffs
  }

  List<Difference> compareBase(){
    def lDiffs = []
    if(a.base != b.base) {
      return [
        new Difference(description:"Restriction base has changed from ${a.base} to ${b.base}." , type: 'restriction', breaks:false)
      ]
    }
    lDiffs
  }

  List<Difference> compareFacets(){
    def diffs = []
    if ( a.facets.isEmpty() && b.facets.isEmpty() ) return []
    def aFacets = a.facets.elementName
    def bFacets = b.facets.elementName
    def removed  = aFacets - bFacets
    def added = bFacets - aFacets 
    removed.each{
      diffs << new Difference(description:"Facet $it removed.", type: 'facet', breaks: false)
    }
    added.each{
      diffs << new Difference(description:"Facet $it added.", type: 'facet', breaks: false)
    }
    (aFacets).intersect(bFacets).each { fName ->
      def aF = a.facets.find{it.elementName == fName}
      def aFV = aF.value 
      def bF = b.facets.find{it.elementName == fName}
      def bFV = bF.value 
      if(aFV != bFV) diffs << new Difference(description:"Value of $fName changed from ${aFV} to ${bFV}.", type: 'facet', breaks: false)
    }
    
    diffs
  }
}

