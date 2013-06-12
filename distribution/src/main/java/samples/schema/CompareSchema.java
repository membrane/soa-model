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

package samples.schema;

import java.util.List;
import com.predic8.schema.Schema;
import com.predic8.schema.SchemaParser;
import com.predic8.schema.diff.SchemaDiffGenerator;
import com.predic8.soamodel.Difference;

public class CompareSchema {

  public static void main(String[] args) {
    compare();
  }
  
  private static void compare(){
    SchemaParser parser = new SchemaParser();

    Schema schema1 = parser.parse("samples/diff/original/article.xsd");

    Schema schema2 = parser.parse("samples/diff/modified/article.xsd");

    SchemaDiffGenerator diffGen = new SchemaDiffGenerator(schema1, schema2);
    List<Difference> lst = diffGen.compare();
    for (Difference diff : lst) {
    	System.out.println(diff.dump());
    }
  }
  
}
