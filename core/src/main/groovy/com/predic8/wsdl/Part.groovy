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

package com.predic8.wsdl;

import groovy.xml.*
import groovy.namespace.*

import javax.xml.namespace.QName as JQName

import com.predic8.schema.*
import com.predic8.soamodel.*
import com.predic8.wsi.*
import com.predic8.xml.util.PrefixedName

class Part extends WSDLElement{

  public static final JQName ELEMENTNAME = new JQName(Consts.WSDL11_NS, 'part')
  
  Element element
  TypeDefinition type
	PrefixedName typePN
	PrefixedName elementPN
	
  protected parseAttributes(token, WSDLParserContext ctx){
    name = token.getAttributeValue( null , 'name')
    if(token.getAttributeValue(null , 'element')) elementPN = new PrefixedName(token.getAttributeValue(null , 'element'))
    if(token.getAttributeValue( null , 'type')) typePN = new PrefixedName(token.getAttributeValue( null , 'type'))
    if(elementPN && typePN) ctx.wsiResults << new WSIResult(rule : 'R2306')
  }
	
	Element getElement() {
		if(element) return element
		if(!elementPN) return
		element = definitions.getElement(getQNameForPN(elementPN))
	}
  
	TypeDefinition getType() {
		if(type) return type
		if(!typePN) return
		type = definitions.getSchemaType(getQNameForPN(typePN))
	}
	
	void create(AbstractCreator creator, CreatorContext ctx) {
		creator.createPart(this, ctx)
	}
	
  String toString() {
    "part[name= $name, type= ${getType()}, element= ${getElement()}]"
  }

	JQName getElementName() {
		ELEMENTNAME
	}

}