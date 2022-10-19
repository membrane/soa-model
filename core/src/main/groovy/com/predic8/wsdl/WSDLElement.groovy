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

package com.predic8.wsdl

import groovy.xml.*
import groovy.namespace.*

import javax.xml.namespace.QName as JQName
import javax.xml.stream.*

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.predic8.soamodel.*
import com.predic8.xml.util.*
//import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.HeaderTokenizer.Token;

abstract class WSDLElement extends XMLElement {

	private static final Logger log = LoggerFactory.getLogger(WSDLElement.class)

	String name
	Definitions definitions
	Documentation documentation

	abstract void create(AbstractCreator creator, CreatorContext ctx)

	protected parseChildren(token, child, WSDLParserContext ctx) {
		switch (token.name) {
			case Documentation.ELEMENTNAME :
				documentation = new Documentation(definitions: definitions, parent : parent)
				documentation.parse(token, ctx)
				break
		}
	}

	String getNamespaceUri() {
		definitions.targetNamespace
	}

	/**
	 * Should return the prefix for the namespace of the element, like wsdl, soap, http & etc.
	 * Used in WSDLCreator.
	 */
	String getPrefix(){
		getPrefix(getElementName().namespaceURI)
	}

	QName getQName() {
		new QName(namespaceUri, name)
	}
}
