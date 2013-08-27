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


class OperationUsageAnalyzerTest extends GroovyTestCase{
  
	Definitions wsdl
	
  void setUp() {
    def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
    wsdl = parser.parse("article/article.wsdl")
  }
	
	void testVisitor() {
		OperationUseVisitorContext ctx = new OperationUseVisitorContext()
		OperationUseVisitor visitor = new OperationUseVisitor()
		wsdl.portTypes.each { pt ->
			pt.operations.each {op ->
				visitor.visitSchema4Operation(op, pt, ctx)
			}
		}
		assert 16 == ctx.elementsInfo.keySet().name.size()
	}
	
	void testAnalyzeOperationUsage() {
//		OperationUsageAnalyzer.analyzeOperationUsage(wsdl).elementsInfo.each { k,v ->
//			print k.name + ' is used in '
//			println v.operation.name
//		}
		assert 16 ==  OperationUsageAnalyzer.analyzeOperationUsage(wsdl).elementsInfo.size()
		assert 7 == OperationUsageAnalyzer.analyzeOperationUsage(wsdl).complexTypesInfo.size()
		assert 2 == OperationUsageAnalyzer.analyzeOperationUsage(wsdl).simpleTypesInfo.size()
	}
	
	void testGetOperationUsageInfos4Element() {
		assert 'create' in OperationUsageAnalyzer.getOperationUsageInfos(wsdl.getElement('tns:create'), wsdl).operation.name
		assert 'ArticleServicePT' in OperationUsageAnalyzer.getOperationUsageInfos(wsdl.getElement('tns:create'), wsdl).portType.name
		assert OperationUsageAnalyzer.getOperationUsageInfos(wsdl.getElement('tns:create'), wsdl)[0].input
		assert !OperationUsageAnalyzer.getOperationUsageInfos(wsdl.getElement('tns:create'), wsdl)[0].output
		assert 'create' in OperationUsageAnalyzer.getOperationUsageInfos(wsdl.getElement('tns:createResponse'), wsdl).operation.name
		assert !OperationUsageAnalyzer.getOperationUsageInfos(wsdl.getElement('tns:createResponse'), wsdl)[0].input
		assert OperationUsageAnalyzer.getOperationUsageInfos(wsdl.getElement('tns:createResponse'), wsdl)[0].output
	}
}
