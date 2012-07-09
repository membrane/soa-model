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

import com.predic8.wsdl.*
import com.predic8.xml.util.*

class ProjectABTest extends GroovyTestCase {
  
  def a
  def b
  
  void setUp(){
    def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
    a = parser.parse(new WSDLParserContext(input:"/diff/project-a/project-service.wsdl"))
    b = parser.parse(new WSDLParserContext(input:"/diff/project-b/project-service.wsdl"))
  }
  
  void testSchemas(){
    def diffs = new WsdlDiffGenerator(a: a, b: b).compare()
    // need some better test, wich make more sense!
    //assertEquals(1218, diffs.dump().size())
  }
  
}
