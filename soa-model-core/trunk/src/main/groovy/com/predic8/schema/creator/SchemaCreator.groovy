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

package com.predic8.schema.creator;

import com.predic8.schema.*
import com.predic8.schema.Sequence as SchemaSequence
import com.predic8.schema.restriction.facet.*
import com.predic8.schema.restriction.BaseRestriction;
import com.predic8.xml.util.*
import org.apache.commons.logging.*

class SchemaCreator extends AbstractSchemaCreator <SchemaCreatorContext>{

  private Log log = LogFactory.getLog(this.class)

  void createSchema(Schema schema, SchemaCreatorContext  ctx) {
    def attrs = [targetNamespace : schema.targetNamespace,
      attributeFormDefault : schema.attributeFormDefault , elementFormDefault : schema.elementFormDefault ]

    declNSifNeeded('xsd',Schema.SCHEMA_NS,attrs,ctx)
      
    schema.namespaceContext.each{ key, value ->
      if ( value != Schema.SCHEMA_NS )
      declNSifNeeded(key,value,attrs,ctx)
    }
    
    attrs.remove("xmlns:")
    builder.'xsd:schema'(attrs) {
      schema.annotation?.create(this, ctx)
      schema.imports.each {
        it.create(this, ctx)
      }
      schema.elements.each{
        it.create(this, ctx)
      }
      schema.complexTypes.each {
        it.create(this, ctx)
      }
      schema.simpleTypes.each{
        it.create(this, ctx)
      }
      schema.groups.each {
        it.create(this, ctx)
      }
      schema.attributes.each {
        it.create(this, ctx)
      }
      schema.attributeGroups.each {
        it.create(this, ctx)
      }
    }
  }

  void createImport(Import imp, SchemaCreatorContext  ctx) {
    def attrs = [namespace : imp.namespace]
    if ( imp.schemaLocation ) {
      attrs['schemaLocation'] = imp.schemaLocation
    }
    builder.'xsd:import'(attrs)
  }

  void createElement(Element element, SchemaCreatorContext  ctx){
    def attrs = [:]
    declNSifNeeded('xsd',Schema.SCHEMA_NS,attrs,ctx)

    if ( element.ref ) {
      attrs['ref'] = element.getTypeString(element.ref)      
    } else {
      attrs['name'] = getDisplayName(element.name,'definitions.allSchemas.elements.name',ctx.error )
    }
    attrs.putAll(getTypeAttribute(element, ctx))
    if(element.minOccurs != (String)1) {attrs.put('minOccurs' , element.minOccurs)}
    if(element.maxOccurs != (String)1) {attrs.put('maxOccurs' , element.maxOccurs)}
    if(element.arrayType) attrs['wsdl:arrayType'] = element.arrayType
    builder.'xsd:element'(attrs){
      element.annotation?.create(this, ctx)
      element.embeddedType?.create(this, ctx)
    }
  }

  void createComplexType(ComplexType complexType, SchemaCreatorContext  ctx){
    def attrs = [:]

    declNSifNeeded('xsd',Schema.SCHEMA_NS,attrs, ctx)

    if(complexType.qname) {
      attrs['name'] = getDisplayName(complexType.qname.getLocalPart(), 'definitions.allSchemas.complexTypes.qname.localPart', ctx.error)
    }
    builder.'xsd:complexType'(attrs){
      if(complexType.annotation) {
        complexType.annotation?.create(this, ctx)
      }
      complexType.model?.create(this, ctx)
      complexType.attributes.each {  attr ->
        attr.create(this, ctx)
      }
      complexType.attributeGroups.each { attrG ->
        attrG.create(this, ctx)  
      }
    }
  }

  void createSimpleContent(SimpleContent simpleContent, SchemaCreatorContext  ctx){
    builder.'xsd:simpleContent'() {
      simpleContent.extension?.create(this, ctx)
      simpleContent.restriction?.create(this, ctx)
    }
  }

  void createComplexContent(ComplexContent complexContent, SchemaCreatorContext ctx){
    builder.'xsd:complexContent'() {
      complexContent.derivation?.create(this, ctx)
    }
  }

  void createGroup(Group group, SchemaCreatorContext  ctx){
    def attrs = [:]
    if(group.minOccurs) attrs['minOccurs'] = group.minOccurs
    if(group.maxOccurs) attrs['maxOccurs'] = group.maxOccurs

    declNSifNeeded('xsd',Schema.SCHEMA_NS,attrs,ctx)

    if(group.qname) {
      attrs['name'] = getDisplayName(group.qname.getLocalPart(), 'definitions.allSchemas.groups.qname.localPart', ctx.error)
    }
    builder.'xsd:group'(attrs){
      group.annotation?.create(this, ctx)
      group.model.create(this, ctx)
    }
  }

  void createGroupRef(GroupRef groupRef, SchemaCreatorContext  ctx){
    builder.'xsd:group'(ref:groupRef.getTypeString(groupRef.ref))
  }

  //TODO next 3 methods are almost the same!
  void createSequence(SchemaSequence sequence, SchemaCreatorContext  ctx){
    def attrs = [:]
    if(sequence.minOccurs) attrs['minOccurs'] = sequence.minOccurs
    if(sequence.maxOccurs) attrs['maxOccurs'] = sequence.maxOccurs
    builder.'xsd:sequence'(attrs) {
      sequence.particles.each {
        it.create(this, ctx)
      }
    }
  }

  void createAll(All all, SchemaCreatorContext  ctx){
    def attrs = [:]
    if(all.minOccurs) attrs['minOccurs'] = all.minOccurs
    if(all.maxOccurs) attrs['maxOccurs'] = all.maxOccurs
    builder.'xsd:all'(attrs) {
      all.particles.each {
        it.create(this, ctx)
      }
    }
  }

  void createChoice(Choice choice, SchemaCreatorContext  ctx){
    def attrs = [:]
    if(choice.minOccurs) attrs['minOccurs'] = choice.minOccurs
    if(choice.maxOccurs) attrs['maxOccurs'] = choice.maxOccurs
    builder.'xsd:choice'(attrs) {
      choice.particles.each {
        it.create(this, ctx)
      }
    }
  }

  void createUnion(Union union, SchemaCreatorContext  ctx) {
    def attrs = [:]
    if(union.memberTypes){
      attrs['memberTypes'] = union.memberTypes.collect { 
        def prefix = it.namespaceURI == Schema.SCHEMA_NS ? 'xsd' : union.getPrefix(it.namespaceURI) 
        "$prefix${prefix?':':''}${it.localPart}"
      }.join(' ')
    }
    builder.'xsd:union'(attrs){
      union.simpleTypes.each {
        it.create(this,ctx)
      }
    }
  }

  void createSimpleType(SimpleType simpleType, SchemaCreatorContext  ctx){
    def attrs = [:]

    declNSifNeeded('xsd',Schema.SCHEMA_NS,attrs,ctx)

    if(simpleType.qname) {
      declNSifNeeded(simpleType.getPrefix(simpleType.schema.targetNamespace),simpleType.schema.targetNamespace,attrs,ctx)
      attrs['name'] = getDisplayName(simpleType.qname.localPart, 'definitions.allSchemas.simpleTypes.qname.localPart', ctx.error)
    }
    builder.'xsd:simpleType'(attrs) {
      simpleType.annotation?.create(this, ctx)
      simpleType.union?.create(this,ctx)
      simpleType.restriction?.create(this, ctx)
      simpleType.list?.create(this, ctx)
    }
  }

  void createSimpleRestriction(BaseRestriction restriction, SchemaCreatorContext  ctx){
    def prefix = restriction.base.namespaceURI == Schema.SCHEMA_NS ? 'xsd' : restriction.getPrefix(restriction.base.namespaceURI) 
    builder.'xsd:restriction'(base : "$prefix${prefix?':':''}${restriction.base.localPart}"){
      restriction.facets.each{
        it.create(this, ctx)
      }
    }
  }

  void createExtension(Extension extension, SchemaCreatorContext  ctx){
    def prefix = extension.base.namespaceURI == Schema.SCHEMA_NS ? 'xsd' : extension.getPrefix(extension.base.namespaceURI)
    builder.'xsd:extension'(base : "$prefix${prefix?':':''}${extension.base.localPart}"){
      extension.model?.create(this, ctx)
      extension.attributes.each{
        it.create(this,ctx)
      }
      extension.attributeGroups.each{
        it.create(this,ctx)
      }
    }
  }

  void createComplexContentRestriction(Restriction restriction, SchemaCreatorContext  ctx){
    builder.'xsd:restriction'(base : restriction.getTypeString(restriction.base)){
      restriction.model?.create(this, ctx)
      restriction.attributes.each{
        it.create(this,ctx)
      }
    }
  }

  void createAttribute(Attribute attr, SchemaCreatorContext  ctx){
    def attribs = [:]
    if(attr.ref) {
      def prefix = attr.ref.namespaceURI == Schema.SCHEMA_NS ? 'xsd' : attr.getPrefix(attr.ref.namespaceURI)
      attribs['ref'] = "$prefix${prefix?':':''}${attr.ref.localPart}" 
    }
    if(attr.name) { attribs['name'] = attr.name }
    if(attr.fixed) { attribs['fixed'] = attr.fixed }
    if(attr.use != 'optional') {attribs['use']=attr.use}
    if(attr.defaultValue) {attribs['default']=attr.defaultValue}
    if(attr.form) {attribs['form']=attr.form}
    if(attr.arrayType) {attribs['wsdl:arrayType'] = attr.arrayType}
    attribs.putAll(getTypeAttribute(attr, ctx))

    builder.'xsd:attribute'(attribs) {
      attr.simpleType?.create(this, ctx)
    } 
  }
  
  void createAttributeGroup(AttributeGroup attrG, SchemaCreatorContext  ctx) {
    def attribs = [:]
    if(attrG.ref) {
      def prefix = attrG.ref.namespaceURI == Schema.SCHEMA_NS ? 'xsd' : attrG.getPrefix(attrG.ref.namespaceURI)
      attribs['ref'] = "$prefix${prefix?':':''}${attrG.ref.localPart}"
    }
    if(attrG.name) { 
      attribs['name'] = attrG.name 
    }
    builder.'xsd:attributeGroup'(attribs) {
      attrG.attributes.each {
        it.create(this, ctx) 
      }
      attrG.attributeGroups.each {
        it.create(this, ctx)
      }
    }
  }

  void createEnumerationFacet(EnumerationFacet facet, SchemaCreatorContext  ctx){
    facet.values.each{
      builder.'xsd:enumeration'(value: it)
    }
  }

  void createList(SchemaList list, SchemaCreatorContext  ctx){
    builder.'xsd:list'(itemType: list.itemType)
  }

  void createLengthFacet(LengthFacet facet, SchemaCreatorContext  ctx){
    facet.values.each{
      builder.'xsd:length'(value: it)
    }
  }

  void createMaxLengthFacet(MaxLengthFacet facet, SchemaCreatorContext  ctx){
    builder.'xsd:maxLength'(value: facet.value)
  }

  void createMinLengthFacet(MinLengthFacet facet, SchemaCreatorContext  ctx){
    builder.'xsd:minLength'(value: facet.value)
  }

  void createMaxInclusiveFacet(MaxInclusiveFacet facet, SchemaCreatorContext  ctx){
    builder.'xsd:maxInclusive'(value: facet.value)
  }

  void createMinInclusiveFacet(MinInclusiveFacet facet, SchemaCreatorContext  ctx){
    builder.'xsd:minInclusive'(value: facet.value)
  }

  void createMaxExclusiveFacet(MaxExclusiveFacet facet, SchemaCreatorContext  ctx){
    builder.'xsd:maxExclusive'(value: facet.value)
  }

  void createMinExclusiveFacet(MinExclusiveFacet facet, SchemaCreatorContext  ctx){
    builder.'xsd:minExclusive'(value: facet.value)
  }

  void createTotalDigitsFacet(TotalDigitsFacet facet, SchemaCreatorContext  ctx){
    builder.'xsd:totalDigits'(value: facet.value)
  }

  void createFractionDigits(FractionDigits facet, SchemaCreatorContext  ctx){
    builder.'xsd:fractionDigits'(value: facet.value)
  }

  void createPatternFacet(PatternFacet facet, SchemaCreatorContext  ctx){
    builder.'xsd:pattern'(value: facet.value)
  }

  void createWhiteSpaceFacet(WhiteSpaceFacet facet, SchemaCreatorContext  ctx){
    builder.'xsd:whiteSpace'(value: facet.value)
  }

  void createAnnotation(Annotation annotation, SchemaCreatorContext  ctx){
    def attrs = [:]
    if(annotation.id) attrs['id'] = annotation.id
    builder.'xsd:annotation'(attrs){
      annotation.contents.each{
        it.create(this, ctx)
      }
    }
  }

  void createDocumentation(Documentation documentation, SchemaCreatorContext  ctx){
    def attrs = [:]
    if(documentation.id) attrs['id'] = documentation.id
    if(documentation.source) attrs['source'] = documentation.source
    if(documentation.lang) attrs['lang'] = documentation.lang
    builder.'xsd:documentation'(attrs, documentation.content)
  }

  void createAppinfo(Appinfo appinfo, SchemaCreatorContext  ctx){
    def attrs = [:]
    if(appinfo.source) attrs['source'] = appinfo.source
    builder.'xsd:appinfo'(attrs, appinfo.content)
  }

  /**
   *TODO refactor method!
   */
  public static String escapeMarkup(String str) {
    str = str.replaceAll(/\[link href='(.*?)'\]/){all, group1 -> "<a href='$group1'>"}
    str.replaceAll(/\[\/link\]/){ "</a>" }
  }

  /**
   * schemaElement can be an element or an attribute.
   */
  private Map getTypeAttribute(schemaElement, ctx){
    if(!schemaElement.type) return [:]

    def attrs = [:]
    log.debug "element ${schemaElement.name} with type ${schemaElement.type} and uri ${schemaElement.type.namespaceURI}"
    def prefix = schemaElement.getPrefix(schemaElement.type.namespaceURI) ?: ''
    
    if ( schemaElement.type.namespaceURI == Schema.SCHEMA_NS ) prefix = 'xsd'
      
    declNSifNeeded(prefix,schemaElement.type.namespaceURI,attrs,ctx)
    if(ctx.createLinks && schemaElement.type.namespaceURI != Schema.SCHEMA_NS) {
      def type = schemaElement.schema.getType(schemaElement.type)
      if(type instanceof ComplexType)
      attrs['type'] = "[link href='complextype?schema=${ctx.getSchemaId(type.qname)}&complextype=${schemaElement.type.localPart}']$prefix${prefix?':':''}${schemaElement.type.localPart}[/link]"
      else if(type instanceof SimpleType)
      attrs['type'] = "[link href='simpletype?schema=${ctx.getSchemaId(type.qname)}&simpletype=${schemaElement.type.localPart}']$prefix${prefix?':':''}${schemaElement.type.localPart}[/link]"
    } else {
      attrs['type'] = "$prefix${prefix?':':''}${schemaElement.type.localPart}"
    }
    attrs
  }
    
}