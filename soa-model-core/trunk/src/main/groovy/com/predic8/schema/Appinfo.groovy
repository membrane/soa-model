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

package com.predic8.schema

import com.predic8.soamodel.CreatorContext 
import javax.xml.stream.*

class Appinfo extends SchemaComponent {

  def source
  def content = ''

  protected parseAttributes(token, params){
    source = token.getAttributeValue( null , 'source')
  }

  protected parseText(text) {
    content += text
  }

  def create(creator, CreatorContext ctx){
    creator.createAppinfo(this, ctx)
  }

  protected getElementName(){
    'appinfo'
  }

  public boolean equals(obj){
    if(this.is(obj)) {
      return true
    }
    if( !obj || (obj.getClass() != this.getClass())) {
      return false
    }
    if(source != obj.source) return false
    content == obj.content
  }

  public int hashCode()
  {
    1360 + content.hashCode()
  }

}

