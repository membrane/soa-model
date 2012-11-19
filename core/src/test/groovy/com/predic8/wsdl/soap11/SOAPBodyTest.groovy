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

package com.predic8.wsdl.soap11

import com.predic8.wsdl.*
import com.predic8.xml.util.*

class SOAPBodyTest extends GroovyTestCase{

  def wsdl

  void setUp(){
    def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
    wsdl = parser.parse(new WSDLParserContext(input:"/RPCLiteralSample.wsdl"))
  }

  void testGetMessage(){
    assertEquals 4, (wsdl.getBinding('RPCLiteralSampleBinding').getOperation('addPerson').input.bindingElements[0].parts).size()
  }
	
}

