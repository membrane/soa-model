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
import javax.xml.stream.*
import com.predic8.xml.util.*
import com.predic8.soamodel.XMLElement
import javax.xml.namespace.QName as JQName
import org.apache.commons.logging.*
import com.predic8.soamodel.Consts
//import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.HeaderTokenizer.Token;

abstract class WSDLElement extends XMLElement {

  String name
  Definitions definitions
  Documentation documentation
  private Log log = LogFactory.getLog(this.class)
  
  def abstract create(creator , context)
  
  QName getTypeQName(String type, token) {
    if ( type ) {
      def preName = new PrefixedName(type)
      def uri = getNamespace(preName.prefix)
      if ( uri == '' && preName.prefix == '' ) uri = schema.targetNamespace //take targetnamespace if no defaultnamespace and no prefix 
      if ( uri == null ) {
        log.warn "Can not find uri for type [${type}]"
      }
      log.debug "resolving [$type] as ${new QName(uri,preName.localName, preName.prefix)}"
      return new QName(uri,preName.localName, preName.prefix)
    }    
  }

  protected parseChildren(token, child, params) {
    switch (token.name) {
      case Documentation.ELEMENTNAME :
      documentation = new Documentation(definitions: definitions, parent : parent)
      documentation.parse(token, params)
    }
  }

  JQName getElementName() {
    ELEMENTNAME
  }
}
