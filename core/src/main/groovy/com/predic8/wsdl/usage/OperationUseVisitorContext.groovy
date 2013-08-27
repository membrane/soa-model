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

	public void updateCompexTypes(ComplexType ct) {
		if(complexTypesInfo[ct]) update(complexTypesInfo, ct)
		else complexTypesInfo[ct] = [opUsage]
	}
	
	public void updateSimpleTypes(SimpleType st) {
		if(simpleTypesInfo[st]) update(simpleTypesInfo, st)
		else simpleTypesInfo[st] = [opUsage]
	}
	
	public void updateElements(Element e) {
		if( elementsInfo[e]) update(elementsInfo, e)
		else elementsInfo[e] = [opUsage]
	}
	
	//Update the list of the give schema component with the actual opUsage  
	private void update(info, sc) {
		OperationUsage tempOpUsage = info[sc].find {it.operation == opUsage.operation}
		if(tempOpUsage) {
			tempOpUsage.input = (tempOpUsage.input || opUsage.input)
			tempOpUsage.output = (tempOpUsage.output || opUsage.output)
			tempOpUsage.output = (tempOpUsage.fault || opUsage.fault)
		} else {
			info[sc] << opUsage
		}
	}
		
	public Object clone() {
		new OperationUseVisitorContext(opUsage: opUsage, elementsInfo: elementsInfo, complexTypesInfo: complexTypesInfo, simpleTypesInfo: simpleTypesInfo)
	}
}

