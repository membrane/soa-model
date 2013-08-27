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
import com.predic8.wstool.creator.*
import com.predic8.xml.util.*


class OperationUsageAnalyzer4OTATest extends GroovyTestCase{
  
	Definitions wsdl
	
  void setUp() {
    def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
    wsdl = parser.parse("hotel.wsdl")
  }
	
	void testAnalyzeOperationUsage() {
		assert 150 == OperationUsageAnalyzer.analyzeOperationUsage(wsdl).elementsInfo.size()
		assert 55 == OperationUsageAnalyzer.analyzeOperationUsage(wsdl).complexTypesInfo.size()
		assert 5 == OperationUsageAnalyzer.analyzeOperationUsage(wsdl).simpleTypesInfo.size()
		
//		OperationUsageAnalyzer.analyzeOperationUsage(wsdl).elementsInfo.each { k,v ->
//			if(v.input && v.output) {
//				print k.name + ' is used in '
//				println v.operation.name + "( ${v.input} , ${v.output}, ${v.fault})" 
//			}
//		}
//		OperationUsageAnalyzer.analyzeOperationUsage(wsdl).simpleTypesInfo.each { k,v ->
//			if(v.input && v.output) {
//				print k.name + ' is used in '
//				println v.operation.name + "( ${v.input} , ${v.output}, ${v.fault})" 
//			}
//		}
//		OperationUsageAnalyzer.analyzeOperationUsage(wsdl).complexTypesInfo.each { k,v ->
//			if(v.input && v.output) {
//				print k.name + ' is used in '
//				println v.operation.name + "( ${v.input} , ${v.output}, ${v.fault})" 
//			}
//		}
	}
	
	void testGetOperationUsageInfos() {
		Element element = wsdl.getElement('ota:TPA_Extensions')
		assert 2 == OperationUsageAnalyzer.getOperationUsageInfos(element, wsdl).operation.size()
	}
	
}
