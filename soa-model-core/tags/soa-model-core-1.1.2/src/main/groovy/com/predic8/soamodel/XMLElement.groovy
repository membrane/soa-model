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
import com.predic8.xml.util.*
import org.apache.commons.logging.*

abstract class XMLElement {

  private Log log = LogFactory.getLog(this.class)
  
  protected Map<String, String> namespaces = [:]
  
  XMLElement parent
  
  def parse(token, params){
    parent = params.parent
    parseNamespaces(token)
    parseAttributes(token, params)
    token.next()
    while(token.hasNext()) {
      if(token.startElement) {
        params.parent = this        
        parseChildren(token, token.name.getLocalPart(), params)
      }
      if(token.getEventType() in [XMLStreamReader.CHARACTERS,XMLStreamReader.CDATA,XMLStreamReader.SPACE]) {
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

  protected parseAttributes(token, params){}

  //@TODO Remove child parameter. SchemaParser has to be changed first.
  protected parseChildren(token, child, params){}

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
  
  def getPrefix(){
    getPrefix(ELEMENTNAME.namespaceURI)
  }
  
  
  def getNamespace(prefix) {
    if(prefix == "xml") return Consts.XML_NS
    def res = namespaces[prefix] // Don't use ?: because res == '' should be a valid response
    if ( res == null ) return parent?.getNamespace(prefix) ?: prefix==''?'':null
    res
  }

  QName getTypeQName(String type) {
    if ( !type ) return // Null is OK cause of embedded types.
    
    def preName = new PrefixedName(type)
    def uri = getNamespace(preName.prefix)
    if ( uri == '' && preName.prefix == '' ) return new QName('',type) //throw new RuntimeException("No namespace declared for element ${type}.") //uri = schema.targetNamespace //take targetnamespace if no defaultnamespace and no prefix
    if ( uri == null ) {
      log.error "Can not find uri for type [${type}]"
      throw new RuntimeException("No namespace declared for element ${type}.")
    }
    log.debug "resolving [$type] as ${new QName(uri,preName.localName, preName.prefix)}"
    return new QName(uri,preName.localName, preName.prefix)
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
