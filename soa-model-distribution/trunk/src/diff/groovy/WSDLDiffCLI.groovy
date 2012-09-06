package diff.groovy

import groovy.xml.MarkupBuilder
import com.predic8.soamodel.Difference;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;
import com.predic8.wsdl.diff.WsdlDiffGenerator;
import groovy.util.CliBuilder;

class WSDLDiffCLI {

  static Definitions wsdl1
  static Definitions wsdl2
  def builder

  public static void main(String[] args) {

    def cli = new CliBuilder()
    cli.usage = 'compare -a <first document> -b <second document>'
    cli.h('Show help')
    cli.a('The first document to compare' , args:1)
    cli.b('The second document to compare' , args:1)
    cli.o('Directory where the report is written', args: 1)

    def options = cli.parse(args)

    WSDLDiffCLI w = new WSDLDiffCLI();
    if(options.a && options.b) {
      w.compare(options.a, options.b, options.o ?: 'output')
      println "compared given files!"
    }
    else {
//      println "please make the right input for the wsdl documents!"
//      cli.usage()
//      return
      
      println "compared samples!"
      
      w.compare("resources/diff/1/article.wsdl", "resources/diff/2/article.wsdl", "output");
    }
  }

  public void compare(String input1, String input2, String output){
    WSDLParser parser = new WSDLParser();

    wsdl1 = parser.parse(input1)

    wsdl2 = parser.parse(input2)

    WsdlDiffGenerator diffGen = new WsdlDiffGenerator(wsdl1, wsdl2);
    List<Difference> lst = diffGen.compare();
    
    Diff2Html(input1, input2, lst, "", output);
  }

  private void Diff2Html(String input1, String input2, List<Difference> diffs, String level, String output){
    new File(output).mkdir();
    builder = new MarkupBuilder(new FileWriter("$output/diff.xml"))
    builder.WSDLDiff{
      WSDLa{
        file(input1)
        TargetNamespace(wsdl1.targetNamespace)
        Services{
          wsdl1.services.each { service ->
            Service(service.name)
          }
        }
        Bindings{
          wsdl1.bindings.each { 
            Binding(it.name)
          }
        }
        PortTypes{
          wsdl1.portTypes.each {
            PortType(it.name)
          }
        }
        Messages{
          wsdl1.messages.each {
            Message(it.name)
          }
        }
      }
      WSDLb{
        file(input2)
        TargetNamespace(wsdl2.targetNamespace)
        Services{
          wsdl2.services.each { service ->
            Service(service.name)
          }
        }
        Bindings{
          wsdl2.bindings.each {
            Binding(it.name)
          }
        }
        PortTypes{
          wsdl2.portTypes.each {
            PortType(it.name)
          }
        }
        Messages{
          wsdl2.messages.each {
            Message(it.name)
          }
        }
      }
      Diffs{
        diffs.each{ diff ->
          dump(diff)
        }
      }
    }
  }
  
  def dump(diff) {
    builder.Diff{
      Description(
        "$diff.description"
        )
        diff.diffs.each{
          dump(it)
        }
      }
    }
}
