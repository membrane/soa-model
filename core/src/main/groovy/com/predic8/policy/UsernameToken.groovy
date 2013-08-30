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

class UsernameToken extends PolicyOperator{

  public QName ELEMENTNAME
  public static final QName VERSION12 = new QName(Consts.SP_NS, 'UsernameToken')
  public static final QName VERSION15 = new QName(Consts.SP_NS2, 'UsernameToken')
	String includeToken
	
	protected def parseAttributes( token,  ctx) {
		includeToken = token.getAttributeValue( null , 'IncludeToken')
	}
	
	protected parseChildren(token, child, ctx){
		super.parseChildren(token, child, ctx)
	}
  
  QName getElementName() {
  	ELEMENTNAME
  }
	
	@Override
  public Object create(Object creator, Object context) {
	  // TODO Auto-generated method stub
	  return null;
  }
}

