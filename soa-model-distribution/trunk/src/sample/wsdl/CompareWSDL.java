package sample.wsdl;

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

    Definitions wsdl1 = parser.parse("resources/diff/1/article.wsdl");

    Definitions wsdl2 = parser.parse("resources/diff/2/article.wsdl");

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
