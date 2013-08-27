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

package com.predic8.wsdl.usage

import groovy.xml.*

import com.predic8.schema.*
import com.predic8.wsdl.Definitions
import com.predic8.wsdl.WSDLParser
import com.predic8.wsdl.usage.OperationUsageAnalyzer
import com.predic8.wsdl.usage.OperationUseVisitor
import com.predic8.wsdl.usage.OperationUseVisitorContext
import com.predic8.wstool.creator.*
import com.predic8.xml.util.*


class OperationUsageAnalyzerWithCyclingTypesTest extends GroovyTestCase{
  
	Definitions wsdl
	
  void setUp() {
    def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
    wsdl = parser.parse("cycling-types/cyclingTypes.wsdl")
  }
	
	void testVisitor() {
		OperationUseVisitorContext ctx = new OperationUseVisitorContext()
		OperationUseVisitor visitor = new OperationUseVisitor()
		wsdl.portTypes.each { pt ->
			pt.operations.each {op ->
				visitor.visitSchema4Operation(op, pt, ctx)
			}
		}
		assert 3 == ctx.elementsInfo.keySet().name.size()
	}
}
