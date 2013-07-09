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

package com.predic8.soamodel

import java.util.Map;
import groovy.xml.QName
import javax.xml.stream.*
import javax.xml.namespace.QName as JQName

import com.predic8.wsdl.Definitions;
import com.predic8.xml.util.*
import org.apache.commons.logging.*

abstract class XMLElement {

	private Log log = LogFactory.getLog(this.class)

	/**
	 * Static final String NAMESPACE need to be declared. It will be used to choose the right parser for the element.
	 */
	protected Map<String, String> namespaces = [:]

	XMLElement parent

	def parse(token, ctx){
		parent = ctx.parent
		parseNamespaces(token)
		parseAttributes(token, ctx)
		token.next()
		while(token.hasNext()) {
			if(token.startElement) {
				ctx.parent = this
				parseChildren(token, token.name.getLocalPart(), ctx)
			}
			if(token.getEventType() in [
				XMLStreamReader.CHARACTERS,
				XMLStreamReader.CDATA,
				XMLStreamReader.SPACE
			]) {
				parseText(token.getText())
			}
			if(isEndTagReached(token)){
				token.next()
				break
			}
			if(token.hasNext()) token.next()
		}
		this
	}

	protected parseAttributes(token, ctx){}

	//@TODO Remove child parameter. SchemaParser has to be changed first.
	protected parseChildren(token, child, ctx){}

	protected parseText(text){}

	abstract protected getElementName()

	private isEndTagReached(token){
		if(! token.endElement) return false

		def name = getElementName()
		if(name instanceof JQName) return name == token.name
		else if(name instanceof QName){
			return name.localPart == token.name.localPart && name.namespaceURI == token.name.namespaceURI
		}
		token.name.getLocalPart() == name
	}

	def getPrefix(String uri) {
		if ( uri == '' ) return ''
		if ( uri == Consts.XML_NS) return 'xml'
		def res = namespaceContext.find{it.value == uri}?.key // dont use ?: because res == '' should be a valid response
	}
	
	abstract String getNamespaceUri()

	def getPrefix(){
		getPrefix(getNamespaceUri())
	}

	def getNamespace(prefix) {
		if(prefix == "xml") return Consts.XML_NS
		def res = namespaces[prefix] // Don't use ?: because res == '' should be a valid response
		if ( res == null ) return parent?.getNamespace(prefix) ?: prefix=='' ? '' : null
		res
	}

	QName getTypeQName(String type) {
		if ( !type ) return // Null is OK cause of embedded types.

		PrefixedName preName = new PrefixedName(type)
		getQNameForPN(preName)
	}
	
	public getQNameForPN(PrefixedName pn) {
		String uri = getNamespace(pn.prefix)
		if ( uri == '' && pn.prefix == '' ) return new QName('',pn.localName)
		if ( uri == null ) {
			log.error "Can not find namespace uri for [${pn}]"
			throw new NamespaceNotDeclaredForReferenceException("No namespace declared for element ${pn} in element '${this.name}'.", pn, this)
		}
		log.debug "resolving [$pn] as ${new QName(uri,pn.localName, pn.prefix)}"
		return new QName(uri,pn.localName, pn.prefix)
	}

	String getTypeString(qname){
		def prefix = getPrefix(qname.namespaceURI)
		"$prefix${prefix?':':''}${qname.localPart}"
	}

	protected parseNamespaces(token) {
		token.getNamespaceCount().times {
			namespaces[token.getNamespacePrefix(it) ?: ''] = token.getNamespaceURI(it)?:''
		}
	}

	def getNamespaceContext() {
		if (!parent) return namespaces
		parent.namespaceContext + namespaces
	}

}
