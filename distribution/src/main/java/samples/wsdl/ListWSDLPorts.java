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

public class ListWSDLPorts {

  public static void main(String[] args) {
    WSDLParser parser = new WSDLParser();

    Definitions wsdl = parser.parse("http://www.thomas-bayer.com/axis2/services/BLZService?wsdl");
    
    for(Service service : wsdl.getServices()){
      System.out.println(service.getName());
      for(Port port : service.getPorts()){
        System.out.println(port.getName() + "( " + port.getBinding().getName()+", " + port.getAddress().getLocation() + " )");
      }
    }
    
    System.out.println(wsdl.getAsString());
  }
}