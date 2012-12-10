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
import org.apache.commons.logging.*

class ElementDiffGenerator extends UnitDiffGenerator {

  private Log log = LogFactory.getLog(this.class)
  
  def removed = {new Difference(description:"Element removed.", type: 'element', breaks: true, safe:false)}

  def added = { new Difference(description:"Element added.", type: 'element', breaks: true, safe:false)}

  def changed = { diffs ->
    new Difference(description:"Element ${a.name} has changed:" , type: 'element' ,  diffs : diffs, a: a, b:b)
  }

  protected getTypeAndName() {
     "element ${a.name}"
  }

  List<Difference> compareUnit(){
    log.debug("compareElement")
    def lDiffs = []
    lDiffs.addAll(compareAnnotation())
    lDiffs.addAll(compareType())
    lDiffs.addAll(compareMinMaxOccurs('element'))
//    lDiffs.addAll(compareEmbeddedType())

    lDiffs
  }

  protected compareAnnotation(){
    a.annotation?.compare(generator, b.annotation) ?: []
  }


  protected compareType(){
    if(a.embeddedType && b.embeddedType) return compareEmbeddedType()
    if(a.embeddedType && b.type) return [new Difference(description:"The type of ${typeAndName} has changed from embedded to stand-alone type.", type: 'element', breaks:true)]
    if(a.type && b.embeddedType) return [new Difference(description:"The type of ${typeAndName} has changed from stand-alone to embedded type.", type: 'element', breaks:true)]
    if(a.type != b.type) return [new Difference(description:"The type of ${typeAndName} has changed from ${a.schema.getPrefix(a.type.namespaceURI)}:${a.type.localPart} to ${a.schema.getPrefix(b.type.namespaceURI)}:${b.type.localPart}.", type: 'element', breaks:true)]
    []
  }

  protected compareMinMaxOccurs(eType){
    def lDiffs = []

      def aMinOccurs = a.minOccurs ?: 1 // apply defaults if needed
      def bMinOccurs = b.minOccurs ?: 1 //
      def aMaxOccurs = a.maxOccurs ?: 1 //
      def bMaxOccurs = b.maxOccurs ?: 1 //

      if(aMinOccurs != bMinOccurs){
      lDiffs << new Difference(description:"The attribute minOccurs of ${typeAndName} has changed from ${a.minOccurs} to ${b.minOccurs}.", type: eType, safe:  aMinOccurs >= bMinOccurs, breaks:  aMinOccurs < bMinOccurs)
    }
    if(aMaxOccurs != bMaxOccurs){
      lDiffs << new Difference(description:"The attribute maxOccurs of ${typeAndName} has changed from ${a.maxOccurs} to ${b.maxOccurs}.", type: eType, safe:  aMaxOccurs <= bMaxOccurs, breaks:  aMaxOccurs > bMaxOccurs)
    }
    lDiffs
  }

  private compareEmbeddedType(){
//    if(!a.embeddedType || !b.embeddedType) return []
    a.embeddedType?.compare(generator, b.embeddedType) ?: []
  }

}

