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
import com.predic8.soamodel.CreatorContext 
import com.predic8.wstool.creator.*
import com.predic8.xml.util.PrefixedName
import javax.xml.stream.*

class Group extends SchemaComponent{

  QName qname
  def model
  def minOccurs
  def maxOccurs


  protected parseAttributes(token, params){
    super.parseAttributes(token, params)
    if (name) {
      def preName = new PrefixedName(name)
      qname = new QName(schema.targetNamespace, preName.localName)
    }
    minOccurs = token.getAttributeValue( null , 'minOccurs')
    maxOccurs = token.getAttributeValue( null , 'maxOccurs')
  }

  protected parseChildren(token, child, params){
    switch (child ){
      case 'annotation' :
      annotation = new Annotation(schema: schema)
      annotation.parse(token, params) ; break
      case 'sequence' :
      model = new Sequence(schema:schema)
      //token.nextTag()
      model.parse(token, params) ; break
      case 'all' :
      model = new All(schema:schema)
      model.parse(token, params) ; break
    }
  }

  protected getElementName(){
    // If changed to QName, SchemaDiffGenerator has to be modified.
    'group'
  }

  def create(creator, CreatorContext ctx){
    creator.createGroup(this, ctx.clone())
  }

  def compare(generator, other){
    generator.compareGroups(this, other)
  }
  
  String getPrefixedName(){
    "${schema.getPrefix(qname.namespaceURI)}:${qname.localPart}"
  }

  String toString(){
    "group[qname=$qname,model=$model]"
  }

  public boolean equals(Object obj) {
    if(this.is(obj)) {
      return true
    }
    if( !obj || (obj.getClass() != this.getClass())) {
      return false
    }
    if(obj.schema.targetNamespace != schema.targetNamespace) {
      return false
    }
    if(obj.qname != qname) {
      return false
    }
    true
  }

  public int hashCode()
  {
    (qname ?: "abc").hashCode() + (schema ?: "xsd").hashCode()
  }
}
