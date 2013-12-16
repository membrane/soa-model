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

package com.predic8.soamodel

import groovy.xml.QName

class ValidationError {
	
	/**Gives the targetNamespace of the validated document.
	 * Either wsdl or schema is set. The other one is null. 
	 */
	String wsdlTNS
	String schemaTNS
	Exception cause

	def invalidElement
	
	//Parent is necessary e.g. to find the right part if the part name is not unique.
	def parent
	
	String message
	
	String toString(){
		"ValidationError[message:'$message', cause: ${cause?.class?.simpleName}]"
	}
}
