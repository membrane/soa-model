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
import groovy.xml.QName
import com.predic8.wstool.creator.*

class Union extends SchemaComponent{
  
  def simpleTypes = []
  List<QName> memberTypes = []

  protected parseAttributes(token, params){
    token.getAttributeValue( null , 'memberTypes')?.split(' ').each{
      memberTypes << getTypeQName(it)
    }
  }

  protected parseChildren(token, child, params){
    switch (child ){
      case 'simpleType' :
      def simpleType = new SimpleType(schema:schema)
      simpleType.parse(token, params)
      simpleTypes << simpleType ; break
    }
  }

  public boolean equals(obj) {
    obj && ( this.is(obj) ||
             getClass() == obj.getClass &&
             simpleTypes == obj.simpleTypes )
  }
  
  protected getElementName(){
    'union'
  }
  
  def create(creator, CreatorContext ctx){
    creator.createUnion(this, ctx)
  }
  
  String toString(){
    "union[simpleTypes=$simpleTypes]"
  }
}
