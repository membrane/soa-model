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

import com.predic8.schema.Element
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
		assertTrue(diffs*.dump().toString().contains('Port ProjectServicePort removed.'))
		assertTrue(diffs*.dump().toString().contains('Port ProServicePort added.'))
  }

  void testPortType() {
    def d = getDefinitions()
    d.portTypes[0].name = "ProjectServicePortType"
    def diffs = compare(orig, d)
    assertEquals(1, diffs.size())
		assertTrue(diffs*.dump().toString().contains('PortType ProjectServicePT removed.'))
		assertTrue(diffs*.dump().toString().contains('PortType ProjectServicePortType added.'))
  }

  void testOperation() {
    def d = getDefinitions()
    def operation = new Operation(name:"newOperation")
    d.portTypes[0].operations << operation
    def diffs = compare(orig, d)
		assertTrue(diffs*.dump().toString().contains('Operation newOperation added.'))
    diffs = compare(d, orig)
    assertTrue(diffs*.dump().toString().contains('Operation newOperation removed.'))
  }

  void testPortLocation() {
    def d = getDefinitions()
    d.services[0].ports[0].address.location = "http://newhost.de"
    def diffs = compare(orig, d)
		assertTrue(diffs*.dump().toString().contains('The location of the port ProjectServicePort changed form http://localhost:${HttpDefaultPort}/ProjectService/ProjectServicePort to http://newhost.de.'))
  }

  void testMessageName() {
    def d = getDefinitions()
    d.portTypes[0].operations[0].input.name = "NewInputName"
    def diffs = compare(orig, d)
		assertTrue(diffs*.dump().toString().contains('Input name has changed from input1 to NewInputName.'))
  }

  private def compare(a, b) {
    new WsdlDiffGenerator(a: a, b: b).compare()
  }
}
