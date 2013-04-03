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

/**
 * DiffGenerator for processing 'any' elements.
 */
class AnyDiffGenerator extends ElementDiffGenerator {
    // extends ElementDiffGenerator to inherit max/min and annotation processing

    // Very helpful for understanding this element and its attributes:
    // http://msdn.microsoft.com/en-us/library/ms256043.aspx

    private Log log = LogFactory.getLog(this.class)
	
	private def labelHasChanged, labelAdded, labelAnyElement, labelRemoved, labelHasChenged, labelNamespaceChanged, labelProcessContentLess, labelProcessContentMore
	
    // removing an 'any' is a breaking change
    def removed = {new Difference(description:"${labelAnyElement} ${labelRemoved}.", type: 'any', breaks: true, safe:false)}

    // adding an 'any' is non-breaking
    def added = { new Difference(description:"${labelAnyElement} ${labelAdded}.", type: 'any', breaks: false, safe:true)}

    // changes may be breaking depending on their nature.  processing of attributes will determine...
    def changed = { diffs ->
        new Difference(description:"${labelAnyElement} ${labelHasChanged}:" , type: 'any' ,  diffs : diffs, a: a, b:b, safe: true, breaks: false)
    }

    // array of processContents values in order of stringency from most leniant to most strict
    private ArrayList<String> processContentsStrictness = [ 'skip', 'lax', 'strict' ]

    protected getTypeAndName() {
        "any"
    }
	
	public AnyDiffGenerator(){
		updateLabels()
	}
	
    @Override
    List<Difference> compareUnit(){
        log.debug("compareAny")
        def lDiffs = []
        lDiffs.addAll(compareAnnotation())
        lDiffs.addAll(compareMinMaxOccurs('any'))
        lDiffs.addAll(compareNamespace())
        lDiffs.addAll(compareProcessing())

        lDiffs
    }

    List<Difference> compareNamespace() {

        // ##any is the default
        String aNamespace = (a.namespace ?: '##any').trim()
        String bNamespace = (b.namespace ?: '##any').trim()

        Set aNamespaces = new HashSet<String>(Arrays.asList(aNamespace.split("\\s+")))
        Set bNamespaces = new HashSet<String>(Arrays.asList(bNamespace.split("\\s+")))

        // ASSUMPTIONS:
        // 1) if the new namespace is ##any, the change is safe
        // 2) if the the new namespace is ##other and the old namespace isnt ##any and
        //      doesn't contain ##targetNamespace, we're safe
        // 3) if b has a set of namespace options, they must be a superset of a's namespaces to be safe

        def isDiffSafe = false
        def isDiffBreaks = true

        if (
                bNamespace == '##any' ||  // #1
                ( bNamespace == '##other' && (aNamespace == "##any" || !aNamespaces.contains("##targetNamespace")) ) || // #2
                bNamespaces.containsAll(aNamespaces)  // #3
        ) {
            isDiffSafe = true
            isDiffBreaks = false
        }

        if (!aNamespaces.equals(bNamespaces)) {
            return [new Difference(description:"${labelNamespaceChanged} '${bNamespace}'", type: 'any', breaks: isDiffBreaks, safe: isDiffSafe)]
        }

        []
    }

    List<Difference> compareProcessing() {
        // default value for processContents is 'strict'
        def aProcessContents = a.processContents ?: 'strict'
        def bProcessContents = b.processContents ?: 'strict'

        int aStrictnessIndex = processContentsStrictness.indexOf(aProcessContents);
        int bStrictnessIndex = processContentsStrictness.indexOf(bProcessContents);

        if (aStrictnessIndex > bStrictnessIndex) return [new Difference(description:"${labelProcessContentLess}", type: 'any', breaks: false, safe: true)]
        if (aStrictnessIndex < bStrictnessIndex) return [new Difference(description:"${labelProcessContentMore}", type: 'any', breaks: true, safe: false)]

        []
    }
	    
	    protected def updateLabels(){
	    	
	    	labelNamespaceChanged = bundle.getString("com.predic8.schema.diff.labelNamespaceChanged")
	    			labelAnyElement = bundle.getString("com.predic8.schema.diff.labelAnyElement")
	    			labelHasChanged = bundle.getString("com.predic8.schema.diff.labelHasChanged")
	    			labelAdded = bundle.getString("com.predic8.schema.diff.labelAdded")
	    			labelRemoved = bundle.getString("com.predic8.schema.diff.labelRemoved")
	    			labelProcessContentLess = bundle.getString("com.predic8.schema.diff.labelProcessContentLess")
	    			labelProcessContentMore = bundle.getString("com.predic8.schema.diff.labelProcessContentMore")
	    }

}