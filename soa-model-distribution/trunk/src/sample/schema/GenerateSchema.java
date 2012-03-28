package sample.schema;

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
