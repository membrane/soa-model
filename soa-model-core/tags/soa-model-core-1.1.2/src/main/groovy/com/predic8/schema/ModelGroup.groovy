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

import com.predic8.wstool.creator.*
import com.predic8.xml.util.PrefixedName
import groovy.xml.QName
import javax.xml.namespace.QName as JQName

abstract class ModelGroup extends SchemaComponent{
  
  List<SchemaComponent> particles = []
  def minOccurs
  def maxOccurs

  protected parseAttributes(token, params){
    minOccurs = token.getAttributeValue( null , 'minOccurs')
    maxOccurs = token.getAttributeValue( null , 'maxOccurs')
  }

  protected parseChildren(token, child, params) {
    switch (child ){
      case 'element' : def element = new Element(schema:schema)
      element.parse(token, params)
      particles << element ; break
      case 'sequence' : def sequence = new Sequence(schema:schema)
      sequence.parse(token, params)
      particles << sequence ; break
      case 'choice' : def choice = new Choice(schema:schema)
      choice.parse(token, params)
      particles << choice; break
      case 'group' : def ref = token.getAttributeValue( null , 'ref')
      def group
      if(ref){
        def groupName = new PrefixedName(ref)
        group = new GroupRef( ref : new QName(schema.getNamespace(groupName.prefix), groupName.localName), schema : schema)
      }else{
        group = new Group(schema:schema)
      }
      group.parse(token, params)
      particles << group ; break
      case 'any' : def any = new Any(schema:schema)
      any.parse(token, params)
      particles << any ; break
    }
  }

  List<Element> getElements(){
    particles.findAll{it instanceof com.predic8.schema.Element}
  }
  
  Element getElement(String name) {
    elements.find{it.name == name}
  }
  
  void add(Element element){
    element.parent = this
    particles << element
  }
  
  Element newElement(String name, JQName type){
    def e = new Element(name: name, type: new QName(type.namespaceURI, type.localPart), schema: schema, parent: this)
    particles << e
    e
  }
  
  Element newElement(String name){
    def e = new Element(name: name, schema: schema, parent: this)
    particles << e
    e
  }
  
  Element newElement(String name, String type){
    def e = new Element(name: name, type: new QName(schema.targetNamespace, type), schema: schema, parent: this)
    particles << e
    e
  }
  
  def compare(generator, other){
    throw new RuntimeException("Compare not implemented for ${this.getClass()}")
  }
  
  String toString(){
    "$elementName[particles=$particles]"
  }
}
