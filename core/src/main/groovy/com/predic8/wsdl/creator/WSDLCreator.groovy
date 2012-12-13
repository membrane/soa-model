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

package com.predic8.wsdl.creator;

import com.predic8.wsdl.*
import com.predic8.schema.creator.*
import com.predic8.soamodel.Consts
import groovy.xml.MarkupBuilderHelper 

class WSDLCreator extends AbstractWSDLCreator{
  
  def createDefinitions(definitions, ctx){
    def attrs = ['targetNamespace':getDisplayName(definitions.targetNamespace, 'definitions.targetNamespace', ctx.error)]
    if(definitions.name) attrs['name'] = definitions.name
    
    builder.definitions(attrs + getNamespaceAttributes(definitions)) {
      definitions.documentation?.create(this, ctx)
      definitions.imports.each{
        it.create(this, ctx)
      }
      definitions.types?.create(this, ctx)
      definitions.messages.each {
        it.create(this, ctx)
      }
      definitions.portTypes.each {
        it.create(this, ctx)
      }
      definitions.bindings.each {
        it.create(this, ctx)
      }
      definitions.services.each {
        it.create(this, ctx)
      }
    }
  }

  def createImport(imp, ctx){
    builder.'import'([namespace: imp.namespace, location: imp.location] + getNamespaceAttributes(imp))
  }

  def createTypes(types, ctx){
    builder.types(){
      types.documentation?.create(this, ctx)
      types.schemas.each{
        it.create(new SchemaCreator(builder: builder), new SchemaCreatorContext(ctx.creatorContext))
      }
    }
  }
  
  def createMessage(message, ctx){
    builder.message([name : getDisplayName(message.name, 'definitions.message.name', ctx.error)] + getNamespaceAttributes(message)) {
      message.documentation?.create(this, ctx)
      message.parts.each {
        it.create(this, ctx)
      }
    }
  }
  
  def createPart(part, ctx) {
    def attrs = [name : part.name]
    if(part.element) {attrs.put('element' , part.element)}
    if(part.type) {attrs.put('type' , part.type)}
    builder.part(attrs + getNamespaceAttributes(part))
  }
  
  def createPortType(portType, ctx) {
    builder.portType([name : getDisplayName(portType.name, 'definitions.portTypes.name', ctx.error)] + getNamespaceAttributes(portType)) {
      portType.documentation?.create(this, ctx)
      portType.operations.each{
        createOperation(it, ctx)
      }
    }
  }
  
  def createOperation(operation, ctx) {
    builder.operation([name : getDisplayName(operation.name, 'definitions.operations.name', ctx.error)] + getNamespaceAttributes(operation)){
      operation.documentation?.create(this, ctx)
      operation.input.create(this, ctx)
      operation.output?.create(this, ctx)
      operation.faults.each {
        it.create(this, ctx)
      }
    }
  }
  
  def createBinding(binding, ctx){
    builder.binding([name : binding.name, type: binding.getTypeString(binding.type)] + getNamespaceAttributes(binding)){
      binding.documentation?.create(this, ctx)
      binding.binding?.create(this, ctx)
      binding.operations.each {
        it.parent = binding
        it.create(this, ctx)
      }
    }
  }
  
  def createSoapBinding(binding, ctx){
    builder."${binding.prefix}:binding"([style: binding.style, transport: Consts.SOAP_ENC_NS] + getNamespaceAttributes(binding))
  }
  
  private getProtocolNamespace(protocol){
    if(protocol == 'SOAP11') return Consts.WSDL_SOAP11_NS
    Consts.WSDL_SOAP12_NS
  }

  def createHTTPBinding(binding, ctx){
    builder."${binding.prefix}:binding"([verb: binding.verb] + getNamespaceAttributes(binding))
  }
  
  def createBindingOperation(operation, ctx) {
    builder."operation"([name : operation.name] + getNamespaceAttributes(operation)) {
      operation.operation?.create(this, ctx)
      operation.input?.create(this, ctx)
      operation.output?.create(this, ctx)
      operation.faults.each{
        it.create(this, ctx)
      }
    }
  }

  def createSOAPOperation(operation, ctx){
    def attrs = [soapAction : operation.soapAction]
    if(operation.style) attrs['style'] = operation.style
    builder."${operation.prefix}:operation"(attrs + getNamespaceAttributes(operation))
  }

  def createHTTPOperation(operation, ctx){
    builder."${operation.prefix}:operation"([location : operation.location]  + getNamespaceAttributes(operation))
  }

  def createPortTypeMessage(portTypeMessage, ctx) {
    def attrs = [message: "${portTypeMessage.definitions.targetNamespacePrefix}:${portTypeMessage.message.name}"]
    if(portTypeMessage.name) attrs['name'] = portTypeMessage.name
    builder."${portTypeMessage.ELEMENTNAME.localPart}"(attrs + getNamespaceAttributes(portTypeMessage))
  }

  def createBindingMessage(bindingMessage, ctx){
    def attrs = [:]
    if(bindingMessage.name ) attrs['name'] = bindingMessage.name
    builder."${bindingMessage.ELEMENTNAME.localPart}"(attrs + getNamespaceAttributes(bindingMessage)){
      bindingMessage.bindingElements.each{
        it.create(this, ctx)
      }
    }
  }

  def createSOAPBody(body, ctx){
    def attrs = [use : body.use]
    if(body.parts) attrs['parts'] = body.parts.name.join(' ')
    if(body.encodingStyle) attrs['encodingStyle'] = body.encodingStyle
    if(body.namespace) attrs['namespace'] = body.namespace
    builder."${body.prefix}:body"(attrs + getNamespaceAttributes(body))
  }
  
  def createSOAP11Header(header, ctx){
    def prefix = header.getPrefix(Consts.WSDL_SOAP11_NS)
    def attrs = [message : "${header.definitions.targetNamespacePrefix}:${header.message.name}", use : header.use, part : header.part]
    if(header.encodingStyle) attrs['encodingStyle'] = header.encodingStyle
    if(header.namespace) attrs['namespace'] = header.namespace
    builder."$prefix:header"(attrs + getNamespaceAttributes(header))
  }

  def createSOAPFault(fault, ctx){
    builder."${fault.prefix}:fault"(use: fault.use, name : fault.name)
  }

  def createService(service , ctx) {
    builder.service(name : getDisplayName(service.name, 'definitions.services.name', ctx.error)) {
      service.documentation?.create(this, ctx)
      service.ports.each {
        it.create(this, ctx)
      }
    }
  }
  
  def createPort(port, ctx) {
    builder.port([name: port.name, binding : "${port.definitions.targetNamespacePrefix}:${port.binding.name}"] + getNamespaceAttributes(port)) {
      port.documentation?.create(this, ctx)
      port.address.create(this, ctx)
    }
  }
  
  def createAddress(address, ctx){
    builder."${address.prefix}:address"([location : address.location] + getNamespaceAttributes(address))
  }

  private createDocumentation(Documentation doc, ctx){
    builder.documentation{new MarkupBuilderHelper(builder).yieldUnescaped(doc)}
  }
}