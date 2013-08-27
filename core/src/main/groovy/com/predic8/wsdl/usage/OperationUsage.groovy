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

import com.predic8.schema.creator.SchemaCreatorContext
import com.predic8.wsdl.Operation
import com.predic8.wsdl.PortType

class OperationUsage extends SchemaCreatorContext implements Cloneable {

	Operation operation
	PortType portType
	boolean input
	boolean output
	boolean fault
	
	String toString() {
		"PortType: ${portType.name}, Operation: ${operation.name} (input: $input, output: $output, fault: $fault)"
	}
}

