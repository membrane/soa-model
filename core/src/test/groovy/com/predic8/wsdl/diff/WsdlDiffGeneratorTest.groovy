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

class WsdlDiffGeneratorTest extends GroovyTestCase {
  
  def orig
  
  void setUp() {
    orig = getDefinitions()
  }
  
  private def getDefinitions() {
    def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
    parser.parse(input:"/project-service.wsdl")
  }
  
  void testServiceName() {
    def d = getDefinitions()
    d.services[0].name = "ProService"
    def diffs = compare(orig, d)
    assertEquals(1, diffs.size())
  }

  void testServicePort() {
    def d = getDefinitions()
    d.services[0].ports[0].name = "ProServicePort"
    def diffs = compare(orig, d)
    assertEquals(2, diffs.size())
  }

  void testPortType() {
    def d = getDefinitions()
    d.portTypes[0].name = "ProjectServicePortType"
    def diffs = compare(orig, d)
    assertEquals(2, diffs.size())
    assertNotNull(diffs.find{it.description.contains("ProjectServicePortType")})
  }

  void testOperation() {
    def d = getDefinitions()
    def operation = new Operation(name:"newOperation")
    d.portTypes[0].operations << operation
    def diffs = compare(orig, d)
    assertEquals(1, diffs.size())
    diffs = compare(d, orig)
    assertEquals(1, diffs.size())
  }

  void testPortLocation() {
    def d = getDefinitions()
    d.services[0].ports[0].address.location = "http://newhost.de"
    def diffs = compare(orig, d)
    assertEquals(1, diffs.size())
  }

  void testMessageName() {
    def d = getDefinitions()
    d.portTypes[0].operations[0].input.name = "new input name"
    def diffs = compare(orig, d)
    assertEquals(1, diffs.size())
  }

  void testMessageElement(){
    def d = getDefinitions()
    d.messages[0].parts[0].element = "new Element"
    def diffs = compare(orig, d)
    assertEquals(1, diffs.size())
  }
  
  private def compare(a, b) {
    new WsdlDiffGenerator(a: a, b: b).compare()
  }
}
