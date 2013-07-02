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
import static com.predic8.schema.Schema.*;

public class GenerateConcreteWSDL {

  public static void main(String[] args) {
    
    Schema schema = new Schema("http://predic8.com/add/1/");
    schema.newElement("add").newComplexType().newSequence().newElement("summand", INT).setMaxOccurs("unbounded");
    schema.newElement("addResponse").newComplexType().newSequence().newElement("number", INT);
    
    Definitions wsdl = new Definitions("http://predic8.com/wsdl/AddService/1/", "AddService");
    wsdl.addSchema(schema);
    
    PortType pt = wsdl.newPortType("AddPortType");
    Operation op = pt.newOperation("add");
    
    op.newInput("add").newMessage("add").newPart("parameters", "tns:add");
    op.newOutput("addResponse").newMessage("addResponse").newPart("parameters", "tns:addResponse");
    
    Port port = wsdl.newService("AddService").newPort("AddServiceSOAP11Port");
    
    Binding bnd = port.newBinding("AddServiceSOAP11Binding");
    bnd.setType(pt);
    bnd.newSOAP11Binding();
    
    BindingOperation bo = bnd.newBindingOperation("add");
    bo.newSOAP11Operation();
    bo.newInput().newSOAP11Body();
    bo.newOutput().newSOAP11Body();
    
    port.newSOAP11Address("http://www.predic8.com/wsdl/AddService/1/AddService");
    
    System.out.println(wsdl.getAsString());
  }
}