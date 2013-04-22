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
	
	
	public ElementDiffGenerator(){
		updateLabels()
	}

  private Log log = LogFactory.getLog(this.class)
  
  def labelElementRemoved, labelElementAdded, labelElement,  labelHasChanged, labelTypeElement, labelTo,
  	  labelEmbeddedStandAlone, labelEmbedded, labelAttributeMinOccurs, labelAttributeMaxOccurs, labelFrom
  
  def removed = {new Difference(description:"${labelElementRemoved}.", type: 'element', breaks: true, safe:false)}

  def added = { new Difference(description:"${labelElementAdded}.", type: 'element', breaks: true, safe:false)}

  def changed = { diffs ->
    new Difference(description:"${labelElement} ${a.name} ${labelHasChanged}:" , type: 'element' ,  diffs : diffs, a: a, b:b)
  }

  protected getTypeAndName() {
     "element ${a.name}"
  }

  List<Difference> compareUnit(){
    log.debug("compareElement")
    def lDiffs = []
    lDiffs.addAll(generator.compareAnnotation(a.annotation, b.annotation))
    lDiffs.addAll(compareType())
    lDiffs.addAll(compareMinMaxOccurs())
//    lDiffs.addAll(compareEmbeddedType())
    lDiffs
  }
  
  protected compareType(){
    if(a.embeddedType && b.embeddedType) return compareEmbeddedType()
    if(a.embeddedType && b.type) return [new Difference(description:"${labelTypeElement} '${a.name}' ${labelHasChanged} ${labelEmbeddedStandAlone}.", type: 'element', safe: false)]
    if(a.type && b.embeddedType) return [new Difference(description:"${labelTypeElement} '${a.name}' ${labelHasChanged} ${labelEmbedded}.", type: 'element', safe: false)]
    if(a.type != b.type) return [new Difference(description:"${labelTypeElement} '${a.name}' ${labelHasChanged} ${labelFrom} ${a.schema.getPrefix(a.type.namespaceURI)}:${a.type.localPart} ${labelTo} ${a.schema.getPrefix(b.type.namespaceURI)}:${b.type.localPart}.", type: 'element', breaks:true)]
    []
  }

  protected compareMinMaxOccurs(eType = 'element'){
		def lDiffs = []
		if(a.minOccurs != b.minOccurs){
			lDiffs << new Difference(description:"${labelAttributeMinOccurs} ${labelElement} $eType ${a.name ? a.name+' ' : ''} ${labelHasChanged} ${labelFrom} ${a.minOccurs} ${labelTo} ${b.minOccurs}.", type: eType, safe:  a.minOccurs >= b.minOccurs, breaks:  a.minOccurs < b.minOccurs)
		}
		if(a.maxOccurs != b.maxOccurs){
			lDiffs << new Difference(description:"${labelAttributeMaxOccurs} ${labelElement} $eType ${a.name ? a.name+' ' : ''} ${labelHasChanged} ${labelFrom} ${a.maxOccurs} ${labelTo} ${b.maxOccurs}.", type: eType, safe:  a.maxOccurs <= b.maxOccurs, breaks:  a.maxOccurs > b.maxOccurs)
		}
    lDiffs
  }

  private compareEmbeddedType(){
    a.embeddedType?.compare(generator, b.embeddedType) ?: []
  }

  protected def updateLabels(){
	  labelElementRemoved = bundle.getString("com.predic8.schema.diff.labelRemoved")
	  labelElement = bundle.getString("com.predic8.schema.diff.labelElement")
	  labelElementAdded = bundle.getString("com.predic8.schema.diff.labelElementAdded")
	  labelTypeElement = bundle.getString("com.predic8.schema.diff.labelTypeElement")
	  labelHasChanged = bundle.getString("com.predic8.schema.diff.labelHasChanged")
	  labelTypeElement = bundle.getString("com.predic8.schema.diff.labelTypeElement")
	  labelEmbeddedStandAlone = bundle.getString("com.predic8.schema.diff.labelEmbeddedStandAlone")
	  labelEmbedded = bundle.getString("com.predic8.schema.diff.labelEmbedded")
	  labelAttributeMinOccurs = bundle.getString("com.predic8.schema.diff.labelAttributeMinOccurs")
	  labelAttributeMaxOccurs = bundle.getString("com.predic8.schema.diff.labelAttributeMaxOccurs")
	  labelTo = bundle.getString("com.predic8.schema.diff.labelTo")
	  labelFrom = bundle.getString("com.predic8.schema.diff.labelFrom")

  }
  
}

