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

package com.predic8.wsdl

import com.predic8.wsdl.style.*

abstract class AbstractSOAPBinding extends AbstractBinding{
  
	Binding binding
	BindingStyle bindingStyle = new DocumentLiteralStyle()
  String transport = "http://schemas.xmlsoap.org/soap/http"
	

  protected parseAttributes(token, ctx){
    String tempStyle = token.getAttributeValue(null , 'style')
		if(tempStyle == 'rpc') bindingStyle = new RPCStyle()
		else{
			//If not RPC, bindingStyle should be Document Literal as default.
			bindingStyle = new DocumentLiteralStyle()
    	if(tempStyle != 'document') ctx.errors << "The style of binding '${binding.name}' should be 'rpc' or 'document'."
		}
    transport = token.getAttributeValue(null , 'transport')
  }
	
	public getStyle() {
		bindingStyle.value
	}
	
	Map checkStyle() {
		bindingStyle.check(binding)
	}
  
  abstract String getContentType()
  
  abstract String getProtocol()
  
  def create(creator, ctx) {
    creator.createSoapBinding(this, ctx)
  }
}
