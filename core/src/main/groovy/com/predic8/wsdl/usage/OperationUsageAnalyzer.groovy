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

import com.predic8.schema.*
import com.predic8.wsdl.Definitions
import com.predic8.wsdl.Operation

class OperationUsageAnalyzer {

	public static List<OperationUsage> getOperationUsageInfos(Element e, Definitions wsdl) {
		analyzeOperationUsage(wsdl).elementsInfo[e]
	}

	public static List<OperationUsage> getOperationUsageInfos(ComplexType ct, Definitions wsdl) {
		analyzeOperationUsage(wsdl).complexTypesInfo[ct]
	}

	public static List<OperationUsage> getOperationUsageInfos(SimpleType st, Definitions wsdl) {
			analyzeOperationUsage(wsdl).simpleTypesInfo[st]
	}
	
	/**
	 * Analyzes usage information for all operations in the WSDL, respective to the portTypes
	 * and stores it in the context.
	 * @param wsdl
	 * @return OperationUseVisitorContext
	 */
	public static OperationUseVisitorContext analyzeOperationUsage(Definitions wsdl) {
		OperationUseVisitorContext ctx = new OperationUseVisitorContext()
		OperationUseVisitor visitor = new OperationUseVisitor()
		wsdl.portTypes.each { pt ->
			pt.operations.each { op->
				visitor.visitSchema4Operation(op, pt, ctx)
			}
		}
		ctx
	}
	
}