package sample.schema;

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

    Schema schema1 = parser.parse("resources/diff/1/common.xsd");

    Schema schema2 = parser.parse("resources/diff/2/common.xsd");

    SchemaDiffGenerator diffGen = new SchemaDiffGenerator(schema1, schema2);
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
