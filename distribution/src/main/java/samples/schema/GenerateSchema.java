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

import com.predic8.schema.*;
import static com.predic8.schema.Schema.*;

public class GenerateSchema {

  public static void main(String[] args) {

    Schema schema = new Schema("http://predic8.com/schema/person/");
    
    schema.newElement("person", "personType");
   
    ComplexType personType = schema.newComplexType("personType");
    personType.newAttribute("id", INT);
    Sequence seq = personType.newSequence();
    seq.newElement("name", STRING);
    seq.newElement("lastname", STRING);
    seq.newElement("date-of-birth", DATE);
    seq.newElement("address").newComplexType().newSequence().newElement("country", STRING);
    
    System.out.println(schema.getAsString());
  }
}
