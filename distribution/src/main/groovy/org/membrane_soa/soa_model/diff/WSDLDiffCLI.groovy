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
				URL(fixURL(url1))
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
        URL(fixURL(url2))
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
    
    new File(reportFolder).mkdir()
    File xml = new File("$reportFolder/diff-report.xml")
    OutputStream outputStream = new FileOutputStream (xml);
    writer.writeTo(outputStream);
    transform(new ByteArrayInputStream(writer.toByteArray()), 'html')
    transform(new ByteArrayInputStream(writer.toByteArray()), 'txt')
  }

  public String getCliUsage() {
    'wsdldiff <first-wsdl> <second-wsdl> [report directory]'
  }

  public getParser() {
    new WSDLParser()
  }
  
  public getStylesheet(format) {
    "${System.getenv('SOA_MODEL_HOME')}/style/wsdl2"+format+".xslt"
  }

  public getDiffGenerator(doc1, doc2) {
    new WsdlDiffGenerator(doc1, doc2)
  }
}
