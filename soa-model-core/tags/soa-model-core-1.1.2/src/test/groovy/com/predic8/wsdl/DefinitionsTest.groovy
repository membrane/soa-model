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

class DefinitionsTest extends GroovyTestCase {

  def wsdl

  void setUp(){
    def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
    wsdl = parser.parse(new WSDLParserContext(input:"/BLZService.wsdl"))
  }
  
  void testSOAP11Bindings() {
    assertEquals(1, wsdl.getBindings('SOAP11').size())
    assertTrue('BLZServiceSOAP11Binding' in wsdl.getBindings('SOAP11').name)
    assertEquals(['document'], wsdl.getBindings('SOAP11').binding.style)
  }

  void testSOAP12Bindings() {
    assertEquals(1, wsdl.getBindings('SOAP12').size())
    assertTrue('BLZServiceSOAP12Binding' in wsdl.getBindings('SOAP12').name)
    assertEquals(['document'], wsdl.getBindings('SOAP11').binding.style)
  }

  void testHTTPBindings() {
    assertEquals(1, wsdl.getBindings('HTTP').size())
    assertTrue('BLZServiceHttpBinding' in wsdl.getBindings('HTTP').name)
    assertEquals(['POST'], wsdl.getBindings('HTTP').binding.verb)
  }
}

