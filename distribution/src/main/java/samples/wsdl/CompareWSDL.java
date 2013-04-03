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

import java.util.List;
import com.predic8.wsdl.*;
import com.predic8.wsdl.diff.WsdlDiffGenerator;
import com.predic8.soamodel.Difference;

public class CompareWSDL {

  public static void main(String[] args) {
    compare();
  }
  
  private static void compare(){
    WSDLParser parser = new WSDLParser();

    Definitions wsdl1 = parser.parse("samples/diff/1/article.wsdl");

    Definitions wsdl2 = parser.parse("samples/diff/2/article.wsdl");

    WsdlDiffGenerator diffGen = new WsdlDiffGenerator(wsdl1, wsdl2);
    List<Difference> lst = diffGen.compare();
    for (Difference diff : lst) {
      dumpDiff(diff, "");
    }
  }

  private static void dumpDiff(Difference diff, String level) {
    System.out.println(level + diff.getDescription());
    for (Difference localDiff : diff.getDiffs()){
      dumpDiff(localDiff, level + "  ");
    }
  }
}
