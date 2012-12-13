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

import com.predic8.soamodel.Consts;
import groovy.xml.QName
import com.predic8.wsi.*

abstract class BindingElement extends WSDLElement{
  
  String use = 'literal'
  String encodingStyle
  String namespace

  protected parseAttributes(token, params){
    name = token.getAttributeValue(null , 'name')
    use = token.getAttributeValue(null , 'use')
    if(use != 'literal') params.wsiResults << new WSIResult(rule : 'R2706')
    encodingStyle = token.getAttributeValue(null , 'encodingStyle')
    namespace = token.getAttributeValue(null , 'namespace')
  }
}
