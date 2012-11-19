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

import com.predic8.soamodel.CreatorContext 
import com.predic8.wstool.creator.*
import javax.xml.namespace.QName as JQName

class Any extends SchemaComponent {
  
  def namespace
  def id
  def processContents
  String minOccurs = 1
  String maxOccurs = 1
  String content = ''
  
  protected parseAttributes(token, params){
    namespace = token.getAttributeValue( null , 'namespace')
    id = token.getAttributeValue( null , 'id')
    minOccurs = token.getAttributeValue( null , 'minOccurs') ?: 1
    maxOccurs = token.getAttributeValue( null , 'maxOccurs') ?: 1
    processContents = token.getAttributeValue( null, 'processContents')
  }

  protected parseText(text) {
    content += text
  }

  def getElementName(){
    // If changed to QName, SchemaDiffGenerator has to be modified.
    'any'
  }

  def create(creator, CreatorContext ctx){
    creator.createAny(this, ctx.clone())
  }

  def compare(generator, other){
    generator.compareAny(this, other)
  }

  String toString(){
    "any[namespace=$namespace]"
  }
}
