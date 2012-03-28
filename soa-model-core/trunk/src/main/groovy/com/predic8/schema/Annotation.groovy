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
import javax.xml.namespace.QName as JQName

class Annotation extends SchemaComponent {

  def id
  def contents = []

  protected parseAttributes(token, params){
    id = token.getAttributeValue( null , 'id')
  }

  protected parseChildren(token, child, params){
    switch (child ){
      case 'documentation' :
      def documentation = new Documentation(schema : schema)
      documentation.parse(token, params)
      contents << documentation ; break
      case 'appinfo' :
      def appinfo = new Appinfo(schema : schema)
      appinfo.parse(token, params)
      contents << appinfo ; break
    }
  }

  protected getElementName(){
    new JQName(Schema.SCHEMA_NS, 'annotation')
  }

  def getDocumentations(){
    contents.findAll{
      it instanceof Documentation
    }
  }

  def getDocumentation(id){
    documentations.find{
      it?.id == id
    }
  }

  def getAppinfos(){
    contents.findAll{
      it instanceof Appinfo
    }
  }

  def create(creator, CreatorContext ctx) {
    creator.createAnnotation(this, ctx)
  }

  def compare(generator, other){
    generator.compareAnnotation(this, other)
  }

  public boolean equals(obj){
    if(this.is(obj)) {
      return true
    }
    if( !obj || (obj.getClass() != this.getClass())) {
      return false
    }
    contents.equals(obj.contents)
  }

  public int hashCode()
  {
    1360 + contents.hashCode()
  }

}

