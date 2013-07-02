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

import com.predic8.xml.util.*

class DefinitionsConstuctorTest extends GroovyTestCase {

  void testDefaultConsturctor() {
		Definitions wsdl = new Definitions()
		wsdl.targetNamespace = 'com.predic8.test.constructor1'
		assert wsdl.registry.allWsdls.size() == 1
  }
	
  void testDefaultConsturctorWithRegistryInHashmap() {
		//In this case the registry contains one WSDL. The following WSDL will be added in the parse() methods.
		Registry reg = new Registry()
		Definitions blz = new WSDLParser(resourceResolver : new ClasspathResolver()).parse('BLZService.wsdl')
		reg.add(blz)
  	Definitions wsdl = new Definitions(baseDir : '/test2/', registry : reg)
  	wsdl.targetNamespace = 'com.predic8.test.constructor2'
		assert wsdl.registry.allWsdls.size() == 1
		assert wsdl.registry.wsdls.keySet().contains('http://thomas-bayer.com/blz/')
		assert wsdl.registry.getWsdls('http://thomas-bayer.com/blz/').contains(blz)
  }
  
  void testDefaultConsturctorWithNullRegistryInHashmap() {
		//This case (registry = null) should absolutely not happen outside the test environments.
  	Definitions wsdl = new Definitions(baseDir : '/test2/', registry : null)
  	wsdl.targetNamespace = 'com.predic8.test.constructor3'
  	assertNull(wsdl.registry)
  }
  
  void testDefaultConsturctorWithHashmap() {
  	Registry reg = new Registry()
  	reg.add(new WSDLParser(resourceResolver : new ClasspathResolver()).parse('BLZService.wsdl'))
  	Definitions wsdl = new Definitions(baseDir : '/test3/')
  	wsdl.targetNamespace = 'com.predic8.test.constructor4'
		assert wsdl.registry.allWsdls.size() == 1
		assert !wsdl.registry.wsdls.keySet().contains('com.predic8.test.constructor4')
		assert wsdl.registry.wsdls.keySet().size() == 1
		assert wsdl.registry.wsdls.keySet().toString() == '[]'
  }
  
	void testConstructor1() {
		Definitions wsdl = new Definitions('Constructor1', 'wsdl1')
		assert wsdl.registry.allWsdls.size() == 1
		assert wsdl.registry.wsdls.keySet().contains('Constructor1')
		assert wsdl.registry.getWsdls('Constructor1').contains(wsdl)
	}

}

