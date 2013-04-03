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

class ImportsDiffGenerator extends AbstractDiffGenerator{
  
  private List<String> alreadyImportedNamespaces 
  
   def labelImportedSchema, labelRemoved, labelAdded, labelHasChanged
  
  def compare(){
    def diffs = compareAddRemove()
    intersection.each{ ns ->
      diffs.addAll(compareImport(ns))
    }
    diffs
  }
  
  private compareAddRemove(){
    compare(a, b,
        { new Difference(description:"${labelImportedSchema} ${it} ${labelRemoved}.", type: 'import') },
        { new Difference(description:"${labelImportedSchema} ${it} ${labelAdded}.", type: 'import') })
  }

  public ImportsDiffGenerator(){
	  updateLabels()
  }
  
  private compareImport(ns){
    def aSchema = a.find{it.namespace == ns}.importSchema
    def bSchema = b.find{it.namespace == ns}.importSchema
    if ( !aSchema || !bSchema ) return []
    def schemaDiffGenerator = new SchemaDiffGenerator(a:aSchema, b:bSchema, alreadyImportedNamespaces: alreadyImportedNamespaces)
    def lDiffs = schemaDiffGenerator.compare()
    if(lDiffs) {
      return [new Difference(description:"${labelImportedSchema} ${ns} ${labelHasChanged}:" , type: 'import', diffs : lDiffs)]
    }
    []
  }

  private getIntersection(){
    (a.namespace).intersect(b.namespace)
  }
  
  protected def updateLabels(){
	  labelImportedSchema = bundle.getString("com.predic8.schema.diff.labelGroup")
	  labelRemoved = bundle.getString("com.predic8.schema.diff.labelRemoved")
	  labelAdded = bundle.getString("com.predic8.schema.diff.labelAdded")
	  labelHasChanged = bundle.getString("com.predic8.schema.diff.labelHasChanged")

  }

}
