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

import java.util.List;

import javax.xml.namespace.QName

import com.predic8.soamodel.Consts

class Response extends WADLElement {

	public static final QName ELEMENTNAME = new QName(Consts.WADL_NS, 'response')
	
	String status // Could be a like: '200 400'
	List<Integer>	statuses = []
	Request req
	Response res
	
	List<Param> params = []
	List<Representation> representations = []

	protected parseAttributes(token, ctx){
		status = token.getAttributeValue( null , 'status')
	}
	
	protected parseChildren(token, child, ctx) {
		super.parseChildren(token, child, ctx)
		switch (token.name) {
			case Param.ELEMENTNAME :
				def param = new Param(application: application, parent: this)
				param.parse(token, ctx)
				params << param
				break
			case Representation.ELEMENTNAME :
				def representation = new Representation(application: application, parent: this)
				representation.parse(token, ctx)
				representations << representation
				break
		}
	}
	
	String toString() {
		"response[status: $status, params: $params, representations: $representations]"
	}
}