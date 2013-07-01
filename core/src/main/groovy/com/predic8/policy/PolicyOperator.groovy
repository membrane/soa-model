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

		
package com.predic8.policy

import javax.xml.namespace.QName as JQName

import org.apache.commons.logging.*

import com.predic8.soamodel.Consts
import com.predic8.soamodel.XMLElement
import com.predic8.wsdl.Definitions
import com.predic8.wsdl.WSDLElement;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Otherwise;

abstract class PolicyOperator extends XMLElement {

	private Log log = LogFactory.getLog(this.class)

	static final String NAMESPACE = Consts.WSP15_NS
	
	String name
	WSDLElement wsdlElement
	All all
	ExactlyOne eOne
	
	protected parseChildren(token, child, ctx){
		switch (token.name){
			case All.ELEMENTNAME:
				all = new All()
				all.parse(token, ctx) ; break
			case ExactlyOne.ELEMENTNAME:
				eOne = new ExactlyOne()
				eOne.parse(token, ctx) ; break
			default:
				ctx.errors << "Parsing ${token.name} not implemented yet!"
				break
		}
	}
  
	String getNamespaceUri() {
		definitions.targetNamespace
	}

  JQName getElementName() {
    ELEMENTNAME
  }
	
  def abstract create(creator , context)

}
