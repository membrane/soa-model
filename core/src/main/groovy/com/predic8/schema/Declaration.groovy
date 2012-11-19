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

package com.predic8.schema

import groovy.xml.QName

import javax.xml.namespace.QName as JQName

abstract class Declaration extends SchemaComponent {
  QName type
  
  /**
   * for wsdl:arrayType
   */
  String arrayType 
  
  void setType(QName type){ //without this groovy 1.8.5 tries to cast QName to JQName and throws ClassCastException
    this.type = type
  }

  QName setType(JQName type){
    this.type = new QName(type.namespaceURI, type.localPart)
  }
  
  protected parseAttributes(token, params){
    super.parseAttributes(token, params)
    arrayType = token.getAttributeValue( 'http://schemas.xmlsoap.org/wsdl/' , 'arrayType')
  }
  
  String getBuildInTypeName(){
    if(type?.namespaceURI == Schema.SCHEMA_NS) return type.localPart
    if(schema.getType(type)) return schema.getType(type).buildInTypeName
    buildInTypeNameLocal
  }
}