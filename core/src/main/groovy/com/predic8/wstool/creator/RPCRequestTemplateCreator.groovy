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

package com.predic8.wstool.creator;

import com.predic8.schema.BuiltInSchemaType;

import groovy.xml.*
import groovy.namespace.*

class RPCRequestTemplateCreator {
  
  def builder
  def definitions
  
  def createRequest(portTypeName, operationName) {
    def operation = definitions.getPortType(portTypeName).getOperation(operationName)
    def attrs = ['xmlns:ns0':definitions.targetNamespace]
    attrs['xmlns:xsi'] = "http://www.w3.org/2001/XMLSchema-instance"
    builder."ns0:${operation.input.name}"(attrs){
      operation.input.message.parts.each {
        def elemAttrs = ['xmlns:soapenv': "http://schemas.xmlsoap.org/soap/envelope/"]
        elemAttrs['soapenv:encodingStyle'] = "http://schemas.xmlsoap.org/soap/encoding/"
        elemAttrs['xsi:type'] = "xsd:string"
				if(it.type instanceof BuiltInSchemaType) builder."${it.name}"(elemAttrs, TemplateUtil.getTemplateValue(it.type.type))
				else  builder."${it.name}"(elemAttrs, TemplateUtil.getTemplateValue(it.type))
      }
    }
  }
}
