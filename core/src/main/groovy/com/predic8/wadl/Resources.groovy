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

class Resources extends WADLElement {

	public static final QName ELEMENTNAME = new QName(Consts.WADL_NS, 'resources')
	
	String base
	
	List<Resource>	resources = []
	
	protected parseAttributes(token, ctx){
		base = token.getAttributeValue( null , 'base')
	}


	protected parseChildren(token, child, ctx) {
		super.parseChildren(token, child, ctx)
		switch (token.name) {
			case Resource.ELEMENTNAME :
				def rsc = new Resource(application: application, parent: this)
				rsc.parse(token, ctx)
				resources << rsc
				break
		}
	}
	
	public String getFullPath() {
		base
	}
	
	String toString() {
		"resources[base: $base , resources: $resources]"
	}
}