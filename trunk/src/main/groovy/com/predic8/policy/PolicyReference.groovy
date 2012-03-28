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

package com.predic8.policy

import javax.xml.namespace.QName
import com.predic8.soamodel.*

class PolicyReference extends XMLElement{

  public static final QName ELEMENTNAME = new QName(Consts.WSP_NS, 'PolicyReference')

  String uri
  String Digest

  protected parseAttributes(token, params){
    uri = token.getAttributeValue(null , 'URI')
    digest = token.getAttributeValue(null , 'Digest')
  }

  def parse(token, params){
//    println "PolicyReference parse method!"
  }

  QName getElementName() {
    ELEMENTNAME
  }
}

