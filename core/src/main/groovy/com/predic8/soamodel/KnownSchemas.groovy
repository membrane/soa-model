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

class KnownSchemas {

	/*Do not use Consts.* as GString for keys because 
	 * the comparison with string does not work.
	 */ 
	static HashMap<String, String> docs = [
		/* Key is the targetNamespace of the document - Value is the relativ file path to the class path.*/
		'http://schemas.xmlsoap.org/wsdl/' : 'wsdl11.xsd',
		'http://schemas.xmlsoap.org/soap/encoding/' : 'encoding.xsd',
		'http://www.w3.org/2003/05/soap-encoding' : 'soap-encoding.xsd',
		'http://www.w3.org/2005/05/xmlmime' : 'xmlmime2005.xsd',
		'http://www.w3.org/2004/06/xmlmime' : 'xmlmime2004.xsd',
		'http://schemas.microsoft.com/2003/10/Serialization/' : 'serialization.xsd',
		'http://schemas.microsoft.com/2003/10/Serialization/Arrays' : 'arrays.xsd',
		'http://www.w3.org/2005/08/addressing' : 'ws-addr.xsd'
	]
}
