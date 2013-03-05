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

package wsdl;

import com.predic8.wsdl.*;

public class ParsingWSDLFully {

  public static void main(String[] args) {
    WSDLParser parser = new WSDLParser();

    Definitions defs = parser
        .parse("http://wsf.cdyne.com/SpellChecker/check.asmx?wsdl");
    
//    System.out.println(defs.toString());

//    for(Binding bnd : defs.getBindings()) {
//    	System.out.println(bnd);
//    }
    
    System.out.println(defs.getBinding("checkSoap12").getOperation("CheckTextBodyV2").getOperation().getSoapAction());
  }
}
