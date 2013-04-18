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

package samples.wsdl;

import com.predic8.wsdl.*;
import com.predic8.schema.*;
import com.predic8.schema.Import;

public class FullWSDLParser {

  public static void main(String[] args) {
    WSDLParser parser = new WSDLParser();

    Definitions defs = parser
        .parse("http://www.thomas-bayer.com/axis2/services/BLZService?wsdl");
    
    System.out.println("-------------- WSDL Details --------------");
    
    System.out.println("TargenNamespace: " + defs.getTargetNamespace());
    System.out.println("Style: " + defs.getStyle());
    System.out.println("\n");
    System.out.println("Services: ");
    for(Service service : defs.getServices()) {
    	System.out.println("  Service Name: " + service.getName());
    	System.out.println("  Service Potrs: ");
    	for(Port port : service.getPorts()){
      	System.out.println("    Port Name: " + port.getName());
      	System.out.println("    Port Binding: " + port.getBinding().getName());
      	System.out.println("    Port Address Location: " + port.getAddress().getLocation() + "\n");
    	}
    }
    System.out.println("");
    
    System.out.println("Bindings: ");
    for(Binding bnd : defs.getBindings()) {
    	System.out.println("  Binding Name: " + bnd.getName());
    	System.out.println("  Binding Type: " + bnd.getPortType().getName());
    	System.out.println("  Binding Operations: ");
    	for(BindingOperation op : bnd.getOperations()) {
    		System.out.println("    Operation Name: " + op.getName() + "\n");
    	}
    }
    System.out.println("");
    
    System.out.println("PortTypes: ");
    for(PortType pt : defs.getPortTypes()){
    	System.out.println("  PortType Name: " + pt.getName());
    	System.out.println("  PortType Operations: ");
    	for(Operation op : pt.getOperations()) {
    		System.out.println("    Operation Name: " + op.getName());
    		System.out.println("    Operation Input Message: " + op.getInput().getMessage().getQname());
    		System.out.println("    Operation Output Message: " + op.getOutput().getMessage().getQname());
    		if(op.getFaults().size() > 0){
    			System.out.println("    Operation Faults: ");
    			for(Fault fault : op.getFaults()){
    				System.out.println("      Fault Message: " + fault.getMessage().getQname());
    			}
    		}
    	}
    	System.out.println("");
    }
    System.out.println("");
    
    System.out.println("Messages: ");
    for(Message msg : defs.getMessages()){
    	System.out.println("  Message Name: " + msg.getName());
    	System.out.println("  Message Parts: ");
    	for(Part part : msg.getParts()){
    		System.out.println("    Part Name: " + part.getName());
    		System.out.println("    Part Element: " + part.getElement() + "\n");
    	}
    }
    System.out.println("");
    
    System.out.println("Schemas: ");
    for (Schema schema : defs.getSchemas()) {
    	System.out.println("  Schema TargetNamespace: " + schema.getTargetNamespace());
    	System.out.println("  AttributeFormDefault: " + schema.getAttributeFormDefault());
    	System.out.println("  ElementFormDefault: " + schema.getElementFormDefault());
    	if(schema.getImports().size() > 0){
    		System.out.println("  Schema Imports: ");
    		for(Import imp : schema.getImports()) {
    			System.out.println("    Import Namespace: " + imp.getNamespace());
    			System.out.println("    Import Location: " + imp.getSchemaLocation());
    		}
    	}
    	System.out.println("  Schema Elements: ");
    	for(Element e : schema.getAllElements()){
    		System.out.println("    Element Name: " + e.getName());
    	}
	    if(schema.getComplexTypes() != null) {
	    	System.out.println("  Schema ComplexTypes: ");
	    	for(ComplexType ct : schema.getComplexTypes()){
	    		System.out.println("    ComplexType Name: " + ct.getName());
	    	}
	    }
    }
  }
}
