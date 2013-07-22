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

package com.predic8.schema;

import groovy.xml.*
import org.apache.commons.logging.*
import javax.xml.namespace.QName as JQName

import java.io.StringWriter

import com.predic8.schema.creator.*
import com.predic8.soamodel.*
import com.predic8.wsdl.Definitions
import com.predic8.xml.util.PrefixedName
import static com.predic8.soamodel.Consts.SCHEMA_NS

class Schema extends SchemaComponent{
  
  public final static JQName ELEMENTNAME = new JQName(SCHEMA_NS, 'schema')
  public final static JQName STRING = new JQName(SCHEMA_NS, 'string')
  public final static JQName INT = new JQName(SCHEMA_NS, 'int')
  public final static JQName INTEGER = new JQName(SCHEMA_NS, 'integer')
  public final static JQName DATE = new JQName(SCHEMA_NS, 'date')
  
  private Log log = LogFactory.getLog(this.class)

  /**
   * if the schema is embedded in a WSDL document, definitions is a reference 
   * to the schema. Definitions is null if the schema is not embedded in a 
   * WSDL document.
   */
  Definitions definitions
  def resourceResolver
  
  String baseDir = ''
  String targetNamespace = ''
  String attributeFormDefault = "unqualified"
  String elementFormDefault = "unqualified"
  List<Import> imports = []
	/**
	 * includes contains a list of Include objects for each included schema.
	 */
  List<Include> includes = []
	
	/**
	 * includedPaths contains a list of Strings with the real schema location 
	 * and is used to prevent a cycling include circle to come to a deadlock.
	 */
	List<String> includedPaths = []
  List<ComplexType> complexTypes = []
  List<Group> groups = []
  List<SimpleType> simpleTypes = []
  List<Element> elements = []
  Annotation annotation
  List<Attribute> attributes = []
  List<AttributeGroup> attributeGroups = []
  
  Schema(){}
  
  Schema(String tns){
    this.targetNamespace = tns
    namespaces['tns'] = tns
  }
  
  protected parseAttributes(token, ctx){
    targetNamespace = ctx.targetNamespace ?: (token.getAttributeValue( null , 'targetNamespace') ?: '')
    ctx.importedSchemas = ctx.importedSchemas ?: [:]
    ctx.importedSchemas[targetNamespace] = this
    attributeFormDefault = token.getAttributeValue( null , 'attributeFormDefault') ?: "unqualified"
    elementFormDefault = token.getAttributeValue( null , 'elementFormDefault') ?: "unqualified"
  }

  protected parseChildren(token, child, ctx){
    switch (child ){
      case 'annotation' :
      annotation = new Annotation(schema: this)
      annotation.parse(token, ctx) ; break
      case 'complexType':
      def complexType = new ComplexType(schema:this)
      complexType.parse(token, ctx)
      complexTypes << complexType ; break
      case 'group':
      def group = new Group(schema:this)
      group.parse(token, ctx)
      groups << group ;break
      case 'simpleType':
			def simpleType = new SimpleType(schema:this)
      simpleType.parse(token, ctx)
      simpleTypes << simpleType ; break
      case 'element':
      def element = new Element(schema:this,toplevel:true)
      element.parse(token, ctx)
      elements << element ; break
      case 'import':
      def imp = new Import(schema : this)
      ctx.token = token
      imp.parse(token, ctx)
      imports << imp ; break
      case 'include':
      ctx.token = token
      def inc = new Include(schema : this)
      log.debug "including schema $inc"
      inc.parse(token, ctx)
      includes << inc ; break
      case 'attribute' :
      def attribute = new Attribute(schema: this)
      attribute.parse(token, ctx)
      attributes << attribute ; break
      case 'attributeGroup' :
      def attributeGroup = new AttributeGroup(schema: this)
      attributeGroup.parse(token, ctx)
      attributeGroups << attributeGroup ; break
    }
  }

  protected getElementName(){
    'schema'
  }
  
  def asXml(namespaces) {
    def strWriter = new StringWriter()
    namespaces.each { k,v->
      this.namespaces[k] = this.namespaces[k] ?: v 
    }
    def creator = new SchemaCreator(builder : new MarkupBuilder(strWriter))
    create(creator, new SchemaCreatorContext())
    strWriter.toString()
  }

  List<Element> getAllElements() {
    (elements + importedSchemas.elements).flatten()
  }
  
  Element getElement(QName qname){
    allSchemas.elements.flatten().find {
      it.name == qname.localPart && it.schema.targetNamespace == qname.namespaceURI
    }
  }
  
  // Achtung: QName wird nicht verwendet
  Element getElement(String elementName){
    def prefixedName = new PrefixedName(elementName)
    def uri
    if(prefixedName.prefix){
      uri = getNamespace(prefixedName.prefix)
    } else {
      return elements.find{ it.name==elementName }
    }
    allSchemas.elements.flatten().find {
      it.name == prefixedName.localName && it.schema.targetNamespace == uri
    }
  }

  Attribute getAttribute(QName ref){
    attributes.find{it.name == ref.localPart}
  }
  
  Attribute getAttribute(String name){
    attributes.find{it.name == name}
  }
  
  AttributeGroup getAttributeGroup(QName ref){
    allSchemas.attributeGroups.flatten().find{it.name == ref.localPart}
  }
  
  AttributeGroup getAttributeGroup(String name){
    attributeGroups.find{it.name == name}
  }
  
  TypeDefinition getType(QName qname){
    (allSchemas.complexTypes + allSchemas.simpleTypes + allSchemas.groups).flatten().find{
      it.qname == qname
    }
  }
  
  TypeDefinition getType(String typeName){
    getType(new QName(targetNamespace,typeName))
  }
  
  ComplexType getComplexType(String typeName){
    getType(new QName(targetNamespace,typeName))
  }
	
	SimpleType getSimpleType(String typeName){
		getType(new QName(targetNamespace,typeName))
	}

  Group getGroup(QName qname){
    (allSchemas.groups).flatten().find{
      it.qname == qname
    }
  }
  
  List<Schema> getAllSchemas(){
    [this] + importedSchemas
  }
	
  List<Schema> getImportedSchemas(){
    getImportedSchemas([])
  }

  Schema getImportedSchema(targetNamespace) {
    importedSchemas.find { it.targetNamespace==targetNamespace }
  }

  List<Schema> getImportedSchemas(importedSchemas){
    log.debug "imported Schemas: ${importedSchemas.targetNamespace}"
    def schemas = []
    imports.each { imp ->
      def schema
      if(definitions) {
        schema = definitions.localSchemas.find{ it.targetNamespace == imp.namespace}
      }
      if(imp.importSchema) {
        schema = imp.importSchema
      }
      if(schema && !(importedSchemas.contains(schema))) {
        schemas << schema
        schemas.addAll(schema.getImportedSchemas(schemas+importedSchemas))
      }
    }
    schemas
  }
  
  public void add(ComplexType ct){
    ct.parent = this
    ct.schema = this
    complexTypes << ct
  }
  
  ComplexType newComplexType(String name){
    def ct = new ComplexType(qname: new QName(targetNamespace, name), schema: this, parent: this)
    complexTypes << ct
    ct
  }
  
  ComplexType newComplexType(){
    new ComplexType(schema:this, parent: this)
  }
	
	SimpleType newSimpleType(){
		new SimpleType(schema: this, parent: this)
	}
	
	SimpleType newSimpleType(String name){
		def st = new SimpleType(qname: new QName(targetNamespace, name), schema: this, parent: this, name: name)
		simpleTypes << st
		st
	}
  
  Element newElement(String name, JQName type){
    def e = new Element(name: name, type: new QName(type.namespaceURI, type.localPart), schema: this, parent: this)
    elements << e
    e
  }
  
  Element newElement(String name, String type){
    def e = new Element(name: name, type: new QName(targetNamespace, type), schema: this, parent: this)
    elements << e
    e
  }
  
  Element newElement(String name){
    def e = new Element(name: name, schema: this, parent: this)
    elements << e
    e
  }
	
	Schema getSchema() {
		//SchemaComponent.schema is used in parse() and getNamspaceUri() methods
		this
	}
  
  def create(creator, CreatorContext ctx) {
    creator.createSchema(this, ctx.clone())
  }
  
  boolean equals(obj){
    if(!(obj instanceof Schema)) 
    return false
    if(obj.targetNamespace != targetNamespace)
    return false
    if(obj.complexTypes && obj.complexTypes.qname != complexTypes.qname)
    return false
    if(obj.simpleTypes && obj.simpleTypes.qname != simpleTypes.qname)
    return false
    if(obj.elements && obj.elements.name != elements.name)
    return false
    true
  }
  
  String getAsString(){
    StringWriter writer = new StringWriter();
    create(new SchemaCreator(builder:new MarkupBuilder(writer)), new SchemaCreatorContext());
    writer.toString()
  }
  
  String toString(){
    "schema[ baseDir=$baseDir, targetNamespace=$targetNamespace, namespaces=$namespaceContext, imports=$imports, includes=$includes, complexTypes=${complexTypes.name}, simpleTypes=${simpleTypes.name}, elements=${elements.name} ]\n"
  }
}
