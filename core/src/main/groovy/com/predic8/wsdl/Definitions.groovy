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

import com.predic8.schema.*
import com.predic8.xml.util.*
import groovy.xml.MarkupBuilder;
import groovy.xml.QName as GQName
import javax.xml.namespace.QName as JQName
import com.predic8.wsdl.soap11.SOAPBinding as SOAP11Binding
import com.predic8.wsdl.soap12.SOAPBinding as SOAP12Binding
import com.predic8.wsdl.creator.WSDLCreator;
import com.predic8.wsdl.creator.WSDLCreatorContext;
import com.predic8.wsdl.http.HTTPBinding   as HTTPBinding
import com.predic8.soamodel.Consts
import java.io.StringWriter;

class Definitions extends WSDLElement{
  
  public static final JQName ELEMENTNAME = new JQName(Consts.WSDL11_NS, 'definitions')
  
  def resourceResolver
  
  def baseDir
  String targetNamespace = ''
  List<Service> services = []
  Types types = new Types()
  List<Binding> bindings = []
  List<Message> messages = []
  List<PortType> portTypes = []
  List<Import> imports = []
  
  public Definitions() {
  }
  
  public Definitions(String tns, String name) { 
    this.name = name
    this.namespaces[''] = 'http://schemas.xmlsoap.org/wsdl/'
      this.namespaces['wsdl'] = 'http://schemas.xmlsoap.org/wsdl/'
    this.namespaces['tns'] = tns
    this.targetNamespace = tns
  }
  
  Boolean isConcrete() {
    !!getBindings("SOAP11")[0]
  }
  
  String getTargetNamespacePrefix() {
    getPrefix(targetNamespace)
  }
  
  PortType getPortType(String name) {
    portTypes.find{ it.name == name }
  }
  
  PortType getPortType(GQName name) {
//    if(!getImportedWSDL(name.namespaceURI)) return
    (getImportedWSDL(name.namespaceURI).portTypes).flatten().find{it.name == name.localPart}
  }
  
  List<Operation> getOperations() {
    (portTypes.operations).flatten().unique{ it.name }
  }
  
  Operation getOperation(String name,String portType) {
    getPortType(portType).operations.find{ name == it.name }
  }
  
  Operation getOperation(String name, GQName portType) {
    getPortType(portType).operations.find{ name == it.name }
  }
  
  Message getMessage(String name) {
    messages.find { it.name == name }
  }
  
  Message getMessage(GQName name) {
    (getImportedWSDL(name.namespaceURI).messages).flatten().find { it.name == name.localPart}
  }
  
  Element getElement(String name) {
    def prefixedName = new PrefixedName(name)
    types.allSchemas.elements.flatten().find {
      it.name == prefixedName.localName
    }
  }
  
  String getElementNameForOperation(String operation, String portType){
    getOperation(operation,portType).input.message.parts.flatten()[0]?.element
  }
  
  Element getElementForOperation(String operation,String portType){
    def name = getElementNameForOperation(operation,portType)
    if (name) return getElement(name)
    null
  }
  
  List<Binding> getBindings(protocol) {
    bindings.findAll{it.protocol == protocol}
  }
  
  Binding getBinding(String name) {
    bindings.find { it.name == name }
  }
  
  Binding getBinding(GQName name) {
    (getImportedWSDL(name.namespaceURI)).bindings.flatten().find { it.name == name.localPart }
  }
  
  /**
   * Use getSOAP11Binding instead
   */
  @Deprecated
  def getSoapBinding(name) {
    getSoap11Binding(name)
  }
  
  def getSoap11Binding(name) {
    bindings.binding.find { it instanceof SOAP11Binding && it.name == name }
  }
  
  def getSoap12Binding(name) {
    bindings.binding.find { it instanceof SOAP12Binding && it.name == name }
  }
  
  def getHTTPBinding(name) {
    bindings.binding.find { it instanceof HTTPBinding && it.name == name }
  }
  
  protected parseAttributes(token, params){
    targetNamespace = params.targetNamespace ?: token.getAttributeValue( null , 'targetNamespace')
    name = token.getAttributeValue( null , 'name')
  }
  
  protected parseChildren(token, child, params){
    super.parseChildren(token, child, params)
    switch (token.name) {
      case Import.ELEMENTNAME :
        def imp = new Import(definitions : this)
        imp.parse(token, params)
          imports << imp ; break
      case Types.ELEMENTNAME :
        types = new Types(definitions : this)
          types.parse(token, params) ; break
      case Message.ELEMENTNAME :
        def message = new Message(definitions: this)
        message.parse(token, params)
          messages << message ; break
      case PortType.ELEMENTNAME:
        def portType = new PortType(definitions:this)
        portType.parse(token, params)
          portTypes << portType ; break
      case Binding.ELEMENTNAME :
        def binding = new Binding(definitions: this)
        binding.parse(token, params)
          bindings << binding; break
      case Service.ELEMENTNAME :
        def service = new Service(definitions : this)
        service.parse(token, params)
          services << service; break
    }
  }
  
  List<Schema> getSchemas(){
    types?.schemas
  }
  
  Schema getSchema(String targetNamespace){
    types.allSchemas.flatten().find{ it.targetNamespace == targetNamespace }
  }
  
  def getService(GQName name){
    (getImportedWSDL(name.namespaceURI).services).flatten().find{ it.name == name.localPart}
  }
  
  def create(creator, ctx){
    creator.createDefinitions(this, ctx)
  }
  
  List<Definitions> getAllWSDLs(){
    [this] + importedWSDLs
  }
  
  List<Definitions> getImportedWSDLs(){
    getImportedWSDLsInternal([])
  }
  
  protected List<Definitions> getImportedWSDLsInternal(wsdls){
    //log.debug "imported WSDLs: ${wsdls.targetNamespace}"
    def res = []
    imports.each { imp ->
      Definitions wsdl = imp.importedWSDL
      if(wsdl && !(wsdls.contains(wsdl))) {
        res << wsdl
        res.addAll(wsdl.getImportedWSDLsInternal(res + wsdls))
      }
    }
    res
  }
  
  private getImportedWSDL(tns){
    allWSDLs.find{it.targetNamespace == tns}
  }
  
  public void add(Schema schema){
      types.schemas << schema
  }
  
  public Message newMessage(String name){
    def msg = new Message(definitions:this, name:name, parent: this)
    messages << msg
    msg
  }
  
  public PortType newPortType(String name){
    def pt = new PortType(definitions:this, name:name, parent: this)
    portTypes << pt
    pt
  }
  
  public Binding newBinding(String name){
    def bnd = new Binding(definitions:this, name:name, parent: this)
    bindings << bnd
    bnd
  }
  
  public Service newService(String name){
    def service = new Service(name : name, definitions: this, parent: this)
    services << service
    service
  }
  
  String getAsString(){
    StringWriter writer = new StringWriter()
    create(new WSDLCreator(builder: new MarkupBuilder(writer)), new WSDLCreatorContext())
    writer.toString()
  }
  
  String toString() {
    "defintions[ baseDir=$baseDir, targetNamespace=$targetNamespace, namespaces=$namespaceContext, services=$services, documentation=$documentation, schemas=$schemas, bindings=$bindings, messages=$messages, portTypes=$portTypes ]"
  }
}
