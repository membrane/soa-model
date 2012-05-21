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

package com.predic8.soamodel

import com.predic8.schema.*
import com.predic8.schema.restriction.facet.*;

import groovy.xml.QName

abstract class AbstractDiffGenerator {

  def generator
  def a,b
  
  def abstract compare()
  
  protected List<Difference> compare(aObjs, bObjs, rm, add){
    aObjs = aObjs ?: []
    bObjs = bObjs ?: []
    if ( aObjs.isEmpty() && bObjs.isEmpty() ) return []
    
    doCompare(aObjs, bObjs, rm, add, getIDClosure(getSampleObject(aObjs, bObjs)))
  }

  private getSampleObject(aObjs, bObjs){
    aObjs.isEmpty() ? bObjs[0] : aObjs[0]
  }

  private getIDClosure(obj){
    switch(obj){
      case Import: return {it.namespace}
      case ComplexType: return {getFQN(it)}
      case SimpleType: return {getFQN(it)}
      case EnumerationFacet: return {it.value}
//      case MaxLengthFacet: return {it.elementName}
    }
    return {it.name}
  }
  
  protected String getFQN(obj){
    '{' + obj.qname.namespaceURI +'}'+ obj.qname.localPart
  }

  def doCompare(aObjs, bObjs, rm, add, getID){
    def aNames = aObjs.collect{getID(it)}
    def bNames = bObjs.collect{getID(it)}
    def diffs = []
    if(aNames[0]){
      def removed  = aNames - bNames
      removed.each{ name ->
        diffs << rm(aObjs.find{getID(it) == name})
      }
    }
    if(bNames[0]){
      def added = bNames - aNames
      added.each{ name ->
        diffs << add(bObjs.find{getID(it) == name})
      }
    }
    diffs
  }

  def findA(QName qname){
    a.find{it.qname == qname}
  }

  def findB(QName qname){
    b.find{it.qname == qname}
  }
}