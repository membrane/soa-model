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
import com.predic8.soamodel.Consts
import com.predic8.xml.util.PrefixedName

class Representation extends WADLElement {

	public static final QName ELEMENTNAME = new QName(Consts.WADL_NS, 'representation')
	
	String id // Type: xsd:id
	String refElementName
	String mediaType
	String href
	String profile //List of URIs, separated with space
	
	List<Param> params = []

	protected parseAttributes(token, ctx){
		id = token.getAttributeValue( null , 'id')
		refElementName = new PrefixedName(token.getAttributeValue( null , 'element'))
		mediaType = token.getAttributeValue( null , 'mediaType')
		href = token.getAttributeValue( null , 'href') 
		profile = token.getAttributeValue( null , 'profile') 
	}
	
	protected parseChildren(token, child, ctx) {
		super.parseChildren(token, child, ctx)
		switch (token.name) {
			case Param.ELEMENTNAME :
				def param = new Param(application: application, parent: this)
				param.parse(token, ctx)
				params << param
				break
		}
	}
	
	Element getElement() {
		application.grammars.find{it.getElement(refElementName)}.getElement(refElementName)
	}
	
	String toString() {
		"representation[id: $id, element: $elementName, href: $href]"
	}
}