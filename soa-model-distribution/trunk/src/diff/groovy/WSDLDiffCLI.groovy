package diff.groovy

import java.util.List;

import groovy.xml.MarkupBuilder
import com.predic8.soamodel.Difference;
import com.predic8.wsdl.WSDLParser;
import com.predic8.wsdl.diff.WsdlDiffGenerator;

class WSDLDiffCLI extends AbstractDiffCLI{

  public static void main(String[] args) {
    WSDLDiffCLI diffCLI = new WSDLDiffCLI().start(args)
  }

  void Diff2Xml(List<Difference> diffs){
    if(output){
      new File(output).mkdir();
      builder = new MarkupBuilder(new FileWriter("$output/schemaDiff.xml"))
    }
    else{
      builder = new MarkupBuilder()
    }
    builder.WSDLDiff{
      "WSDL-a"{
        URL(input1)
        TargetNamespace(wsdl1.targetNamespace)
        Services{
          wsdl1.services.each { service ->
            Service(service.name)
          }
        }
        Bindings{
          wsdl1.bindings.each {  Binding(it.name) }
        }
        PortTypes{
          wsdl1.portTypes.each { PortType(it.name) }
        }
        Messages{
          wsdl1.messages.each { Message(it.name) }
        }
      }
      "WSDL-b"{
        file(input2)
        TargetNamespace(wsdl2.targetNamespace)
        Services{
          wsdl2.services.each { service ->
            Service(service.name)
          }
        }
        Bindings{
          wsdl2.bindings.each { Binding(it.name) }
        }
        PortTypes{
          wsdl2.portTypes.each { PortType(it.name) }
        }
        Messages{
          wsdl2.messages.each { Message(it.name) }
        }
      }
      Diffs{
        diffs.each{ diff -> dump(diff) }
      }
    }
  }

  public String getCliUsage() {
    'WSDLDiff -a <first document> -b <second document>'
  }

  public getParser() {
    new WSDLParser()
  }

  public getDiffGenerator(doc1, doc2) {
    new WsdlDiffGenerator(doc1, doc2)
  }
}
