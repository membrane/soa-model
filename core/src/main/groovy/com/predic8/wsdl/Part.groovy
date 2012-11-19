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

package com.predic8.wsdl;

import groovy.xml.*
import javax.xml.namespace.QName as JQName
import com.predic8.wsi.*
import com.predic8.schema.*
import com.predic8.soamodel.Consts

class Part extends WSDLElement{

  public static final JQName ELEMENTNAME = new JQName(Consts.WSDL11_NS, 'part')
  
  String element
  QName type
  
  protected parseAttributes(token, params){
    name = token.getAttributeValue( null , 'name')
    element = token.getAttributeValue(null , 'element')
    type = getTypeQName(token.getAttributeValue( null , 'type'), token)
    if(element && type) params.wsiResults << new WSIResult(rule : 'R2306')
  }
  
  def create(creator, ctx) {
    creator.createPart(this, ctx)
  }
  
  String toString() {
    "part[name= $name, type= $type, element= $element]"
  }
}