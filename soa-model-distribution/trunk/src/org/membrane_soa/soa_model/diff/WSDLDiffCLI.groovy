package org.membrane_soa.soa_model.diff

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
    def input = new ByteArrayInputStream(writer.toByteArray())
    transform(input)
  }

  public String getCliUsage() {
    'WSDLDiff -a <first document> -b <second document>'
  }

  public getParser() {
    new WSDLParser()
  }
  
  public getStylesheet() {
    "${System.getenv('SOA_MODEL_HOME')}/style/wsdl/2html.xslt"
  }

  public getDiffGenerator(doc1, doc2) {
    new WsdlDiffGenerator(doc1, doc2)
  }
}
