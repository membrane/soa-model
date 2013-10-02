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
import com.predic8.xml.util.PrefixedName;

class Param extends WADLElement {

	public static final QName ELEMENTNAME = new QName(Consts.WADL_NS, 'param')
	
	String href
	String name
	String style // Possible values: template matrix query header
	String id
	PrefixedName typePN
	def type
	//Beware of the name of attribute 'dfault' instead of default (key word).
	String dfault
	boolean required
	boolean repeating
	String fixed
	String path
	
	List<Option> options = []
	Link link

	protected parseAttributes(token, ctx){
		href = token.getAttributeValue( null , 'href')
		name = token.getAttributeValue( null , 'name')
		style = token.getAttributeValue( null , 'style') ?: 'xs:string'
		id = token.getAttributeValue( null , 'id')
		typePN = new PrefixedName(token.getAttributeValue( null , 'type'))
		dfault = token.getAttributeValue( null , 'default')
		required = (token.getAttributeValue( null , 'required') == 'true') ? true : false
		repeating = (token.getAttributeValue( null , 'repeating') == 'true') ? true : false
		fixed = token.getAttributeValue( null , 'fixed')
		path = token.getAttributeValue( null , 'path')
	}
	
	protected parseChildren(token, child, ctx) {
		super.parseChildren(token, child, ctx)
		switch (token.name) {
			case Option.ELEMENTNAME :
				def option = new Option(application: application, parent: this)
				option.parse(token, ctx)
				options << option
				break
			case Link.ELEMENTNAME :
				link = new Link(application: application, parent: this)
				link.parse(token, ctx)
				break
		}
	}
	
	def getType(){
		if(type) return type
				type = getQNameForPN(typePN) ?: new QName(Consts.SCHEMA_NS,'string')
	}
	
	String toString() {
		"param[name: $name, style: $style, type: $typePN]"
	}
}