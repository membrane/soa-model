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

import com.predic8.schema.ComplexType
import com.predic8.schema.Element
import com.predic8.schema.SchemaComponent;
import com.predic8.schema.SimpleType
import com.predic8.schema.creator.SchemaCreatorContext

class OperationUseVisitorContext extends SchemaCreatorContext implements Cloneable {

	OperationUsage opUsage
	Map<Element, List<OperationUsage>> elementsInfo = [:]
	Map<ComplexType, List<OperationUsage>> complexTypesInfo = [:]
	Map<SimpleType, List<OperationUsage>> simpleTypesInfo = [:]
	
	//List of visited schema components to avoid cycling references.
	List<SchemaComponent> visited = []

	public void updateCompexTypes(ComplexType ct) {
		update(complexTypesInfo, ct)
	}

	public void updateSimpleTypes(SimpleType st) {
		update(simpleTypesInfo, st)
	}

	public void updateElements(Element e) {
		update(elementsInfo, e)
	}

	//Update the list of the give schema component with the actual opUsage
	private void update(info, sc) {
		if(info[sc]) {
			OperationUsage existingOpUsage = info[sc].find {it.operation == opUsage.operation}
			if(existingOpUsage) {
				existingOpUsage.input = (existingOpUsage.input || opUsage.input)
				existingOpUsage.output = (existingOpUsage.output || opUsage.output)
				existingOpUsage.output = (existingOpUsage.fault || opUsage.fault)
			} else {
				info[sc] << opUsage
			}
		} 
		else info[sc] = [opUsage] 
	}	

	public Object clone() {
		new OperationUseVisitorContext(opUsage: opUsage, elementsInfo: elementsInfo, complexTypesInfo: complexTypesInfo, simpleTypesInfo: simpleTypesInfo, visited: visited)
	}
}

