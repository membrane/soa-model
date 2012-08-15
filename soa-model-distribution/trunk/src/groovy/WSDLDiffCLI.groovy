package groovy

import groovy.xml.MarkupBuilder
import com.predic8.soamodel.Difference;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;
import com.predic8.wsdl.diff.WsdlDiffGenerator;

class WSDLDiffCLI {

  static Definitions wsdl1
  static Definitions wsdl2
  
  public static void main(String[] args) {
    compare("resources/diff/1/article.wsdl", "resources/diff/2/article.wsdl", "output");
  }

  public static void compare(String input1, String input2, String output){
    WSDLParser parser = new WSDLParser();

    wsdl1 = parser.parse(input1)

    wsdl2 = parser.parse(input2)

    WsdlDiffGenerator diffGen = new WsdlDiffGenerator(wsdl1, wsdl2);
    List<Difference> lst = diffGen.compare();

    Diff2Html(lst, "", output);
  }

  private static void Diff2Html(List<Difference> diffs, String level, String output){
    def builder = new MarkupBuilder(new FileWriter("$output/diff.html"))
    builder.html{
      body {
        diffs.each{ diff ->
          p{
            b (diff.getDescription())
            diff.getDiffs().each{ lDiff ->
              br()
              span diff.getDescription()
            }
          }
        }
      }
    }
  }
}
