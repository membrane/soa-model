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

import javax.xml.namespace.QName as JQName

import com.predic8.soamodel.CreatorContext;
import com.predic8.wstool.creator.*

class SchemaList extends SchemaComponent {
  
  String itemType;
  SimpleType simpleType
  
  protected parseAttributes(token, params){
    itemType = token.getAttributeValue( null , 'itemType')
  }

  def parseChildren(token, child, params){
    switch (child ){
      case 'simpleType' : simpleType = new SimpleType(schema:schema)
      simpleType.parse(token, params) ; break
    }
  }

  public boolean equals(obj) {
    obj && ( this.is(obj) ||
             getClass() == obj.getClass &&
             itemType == obj.itemType &&
             simpleType == obj.simpleType )
  }

  protected getElementName(){
    new JQName(Schema.SCHEMA_NS, 'list')
  }
  
  def create(creator, CreatorContext ctx){
    creator.createList(this, ctx.clone())
  }
  
  String getBuildInTypeName(){
    itemType.split(':')[1]
  }
  
  String toString(){
    "list[itemType=$itemType]"
  }
}
