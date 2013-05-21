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

package com.predic8.wsdl.diff

import com.predic8.soamodel.Difference
import com.predic8.wsdl.AbstractPortTypeMessage

class PortTypeMessageDiffGenerator extends WsdlDiffGenerator{

	AbstractPortTypeMessage a
	AbstractPortTypeMessage b
	
	public PortTypeMessageDiffGenerator(a, b) {
		this.a = a
		this.b = b
	}
	
	public List<Difference> compare() {
		def diffs = compareDocumentation(a, b)
		if(a.message.name != b.message.name || a.message.namespaceUri != b.message.namespaceUri)
		 diffs << new Difference(description: "$elementTagName message has changed from ${a.message.prefix}:${a.message.name} to ${b.message.prefix}:${b.message.name}", type: 'message', breaks : true)
		else diffs.addAll( compareMessage(a.message, b.message))
		if(diffs) return [new Difference(description:"${elementTagName} has changed:", type:'input', diffs: diffs )]
		[]
	}
	
	protected String getElementTagName() {
		a.elementName.localPart.capitalize()
	}
	
}