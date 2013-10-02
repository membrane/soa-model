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

package com.predic8.wadl

import javax.xml.namespace.QName

import com.predic8.soamodel.Consts

class Method extends WADLElement {

	public static final QName ELEMENTNAME = new QName(Consts.WADL_NS, 'method')
	
	String id
	String name
	String href
	
	Request request
	Response response

	protected parseAttributes(token, ctx){
		name = token.getAttributeValue( null , 'name')
		id = token.getAttributeValue( null , 'id')
		href = token.getAttributeValue( null , 'href')
	}
	
	protected parseChildren(token, child, ctx) {
		super.parseChildren(token, child, ctx)
		switch (token.name) {
			case Request.ELEMENTNAME :
				request = new Request(application: application, parent: this)
				request.parse(token, ctx)
				break
			case Response.ELEMENTNAME :
				response = new Response(application: application, parent: this)
				response.parse(token, ctx)
				break
		}
	}
	
	String toString() {
		"method[name: $name, request: $request, response: $response]"
	}
}