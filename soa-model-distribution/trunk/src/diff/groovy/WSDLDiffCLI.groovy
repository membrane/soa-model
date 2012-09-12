package diff.groovy

import javax.xml.transform.*
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource
import groovy.xml.MarkupBuilder
import com.predic8.soamodel.Difference;
import com.predic8.wsdl.WSDLParser;
import com.predic8.wsdl.diff.WsdlDiffGenerator;

class WSDLDiffCLI extends AbstractDiffCLI{

  public static void main(String[] args) {
    WSDLDiffCLI diffCLI = new WSDLDiffCLI().start(args)
  }

  void Diff2Xml(List<Difference> diffs){
    def writer = new ByteArrayOutputStream()
    builder = new MarkupBuilder(new PrintWriter(writer))
    builder.WSDLDiff{
      "WSDL-a"{
        URL(url1)
        TargetNamespace(doc1.targetNamespace)
        Services{
          doc1.services.each { service ->
            Service(service.name)
          }
        }
        Bindings{
          doc1.bindings.each {  Binding(it.name) }
        }
        PortTypes{
          doc1.portTypes.each { PortType(it.name) }
        }
        Messages{
          doc1.messages.each { Message(it.name) }
        }
      }
      "WSDL-b"{
        URL(url2)
        TargetNamespace(doc2.targetNamespace)
        Services{
          doc2.services.each { service ->
            Service(service.name)
          }
        }
        Bindings{
          doc2.bindings.each { Binding(it.name) }
        }
        PortTypes{
          doc2.portTypes.each { PortType(it.name) }
        }
        Messages{
          doc2.messages.each { Message(it.name) }
        }
      }
      Diffs{
        diffs.each{ diff -> dump(diff) }
      }
    }
//    println writer
    def input = new ByteArrayInputStream(writer.toByteArray())
    transform(input)
  }
  
  public transform(input){
    try {
      TransformerFactory xformFactory = TransformerFactory.newInstance()
      Source xsl = new StreamSource("src/style/wsdl/2html.xslt")
      Transformer stylesheet = xformFactory.newTransformer(xsl)
      Source request  = new StreamSource(input)

      new File(output).mkdir()
      Result response = new StreamResult(new FileWriter("$output/wsdlDiff-result.html"))
      stylesheet.transform(request, response)
    }
    catch (TransformerException e) {
      System.err.println(e);
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
