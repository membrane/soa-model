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

package com.predic8.wsi

import com.predic8.wsdl.*
import com.predic8.xml.util.*
import groovy.test.GroovyTestCase

class WSITest extends GroovyTestCase {

  def wsdl
  def wsiResults

  void setUp(){
    def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
    def ctx = new WSDLParserContext(input:"/wsi/BLZService.wsdl")
    wsdl = parser.parse(ctx)
    wsiResults = ctx.wsiResults
  }

  void testR2306(){
    assertNotNull(wsiResults.find{it.rule == 'R2306'})
  }

  void testR2701(){
    //will be tested by R2702
  }

  void testR2702(){
    assertNotNull(wsiResults.find{it.rule == 'R2702'})
  }

  void testR2706(){
    assertNotNull(wsiResults.find{it.rule == 'R2706'})
  }
}

