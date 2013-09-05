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

import com.predic8.soamodel.*
import com.predic8.wsdl.style.*

abstract class AbstractSOAPBinding extends AbstractBinding{
  
	Binding binding
	private String style 
  String transport = "http://schemas.xmlsoap.org/soap/http"
	

  protected parseAttributes(token, WSDLParserContext ctx){
    style = token.getAttributeValue(null , 'style')
    transport = token.getAttributeValue(null , 'transport')
  }
	
	BindingStyle getBindingStyle() {
		if(style == 'document') return new DocumentLiteralStyle()
		if(style == 'rpc') return new RPCStyle()
		/**If bindingStyle is not defined in SOAP Binding, 
		 * it should be looked up in binding operation.
		 * As default it should be 'document'
		 */
		def opStyle = binding.operations.operation.style.unique()
		if(opStyle.contains('rpc') || 1 < opStyle.size()) return new RPCStyle()
		return new DocumentLiteralStyle()
	}
	
	public getStyle() {
		getBindingStyle().value
	}
	
	Map checkStyle() {
		getBindingStyle().check(binding)
	}
  
  abstract String getContentType()
  
  abstract String getProtocol()
  
  void create(AbstractCreator creator, CreatorContext ctx) {
    creator.createSoapBinding(this, ctx)
  }
}
