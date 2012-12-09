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

    private Log log = LogFactory.getLog(this.class)

    // removing an 'any' is a breaking change
    def removed = {new Difference(description:"Any element removed.", type: 'any', breaks: true, safe:false)}

    // adding an 'any' is non-breaking
    def added = { new Difference(description:"Any element added.", type: 'any', breaks: false, safe:true)}

    // changes may be breaking depending on their nature.  processing of attributes will determine...
    def changed = { diffs ->
        new Difference(description:"Any element has changed:" , type: 'any' ,  diffs : diffs, a: a, b:b, safe: true, breaks: false)
    }

    // array of processContents values in order of stringency from most leniant to most strict
    private ArrayList<String> processContentsStrictness = [ 'skip', 'lax', 'strict' ]

    protected getTypeAndName() {
        "any"
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
        if (a.namespace && !b.namespace) return [new Difference(description:"namespace attribute removed", type: 'any', breaks: false, safe: true)]
        if (!a.namespace && b.namespace) return [new Difference(description:"namespace attribute added", type: 'any', breaks: true, safe: false)]
        if (a.namespace && b.namespace) {
            if (a.namespace != b.namespace) return [new Difference(description:"namespace attribute changed", type: 'any', breaks: true, safe: false)]
        }
        []
    }

    List<Difference> compareProcessing() {
        // default value for processContents is 'strict'
        def aProcessContents = a.processContents ?: 'strict'
        def bProcessContents = b.processContents ?: 'strict'

        int aStrictnessIndex = processContentsStrictness.indexOf(aProcessContents);
        int bStrictnessIndex = processContentsStrictness.indexOf(bProcessContents);

        if (aStrictnessIndex > bStrictnessIndex) return [new Difference(description:"processContents became less strict", type: 'any', breaks: false, safe: true)]
        if (aStrictnessIndex < bStrictnessIndex) return [new Difference(description:"processContents became more strict", type: 'any', breaks: true, safe: false)]

        []
    }

}