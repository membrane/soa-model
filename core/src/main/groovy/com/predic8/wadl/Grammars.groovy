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

import com.predic8.schema.Element
import com.predic8.schema.Schema
import com.predic8.soamodel.Consts

class Grammars extends WADLElement {

	public static final QName ELEMENTNAME = new QName(Consts.WADL_NS, 'grammars')

	List<Include> includes = []
	List<Schema> schemas = []
	
	protected parseChildren(token, child, ctx) {
		super.parseChildren(token, child, ctx)
		switch (token.name) {
			case Include.ELEMENTNAME :
				def inc = new Include(application: application, parent: this)
				inc.parse(token, ctx)
				includes << inc
				break
			case Schema.ELEMENTNAME :
				def schema = new Schema()
				schema.parse(token, ctx)
				schemas << schema
				break
		}
	}
	
	List<Schema> getAllSchemas() {
		schemas + includes.grep{it.schema}.schema
	}
	
	public Element getElement(String name) {
		allSchemas.find{it.getElement(name)}.getElement(name)
	}
	
	String toString() {
		"grammer[docs: $docs, includes: $includes]"
	}
}