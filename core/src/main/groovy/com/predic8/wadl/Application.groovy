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

package com.predic8.wadl

import javax.xml.namespace.QName

import com.predic8.soamodel.Consts

class Application extends WADLElement {

	public static final QName ELEMENTNAME = new QName(Consts.WADL_NS, 'application')

	def resourceResolver

	def baseDir

	Grammars grammars
	List<Resources> rscss = []
	
	WADLElement choiceElement // Could be one of these objects: ResourceType, Method, Representation, Param

	protected parseChildren(token, child, ctx) {
		super.parseChildren(token, child, ctx)
		switch (token.name) {
			case Grammars.ELEMENTNAME :
				grammars = new Grammars(application: this, parent: this)
				grammars.parse(token, ctx)
				break
			case Resources.ELEMENTNAME :
				def rscs = new Resources(application: this, parent: this)
				rscs.parse(token, ctx)
				rscss << rscs
				break
			case ResourceType.ELEMENTNAME :
				choiceElement = new ResourceType(application: this, parent: this)
				choiceElement.parse(token, ctx)
				break
			case Method.ELEMENTNAME :
				choiceElement = new Method(application: this, parent: this)
				choiceElement.parse(token, ctx)
				break
			case Representation.ELEMENTNAME :
				choiceElement = new Representation(application: this, parent: this)
				choiceElement.parse(token, ctx)
				break
			case Param.ELEMENTNAME :
				choiceElement = new Param(application: this, parent: this)
				choiceElement.parse(token, ctx)
				break
		}
	}
	
	String toString() {
		def str = "application[" + (docs? "docs: $docs , " : '') + (grammars? "grammars: $grammars , " : '') + (rscss? "resources: $rscss]" : ']') 
		str
	}
}