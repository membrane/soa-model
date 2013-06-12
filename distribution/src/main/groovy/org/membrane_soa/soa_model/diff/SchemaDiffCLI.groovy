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
import com.predic8.schema.SchemaParser;
import com.predic8.schema.diff.SchemaDiffGenerator;

class SchemaDiffCLI extends AbstractDiffCLI{

  public static void main(String[] args) {
    SchemaDiffCLI diffCLI = new SchemaDiffCLI().start(args)
  }

  void Diff2Xml(List<Difference> diffs){
    def writer = new ByteArrayOutputStream()
    builder = new MarkupBuilder(new PrintWriter(writer))
    builder.SchemaDiff{
      "Original"{
        URL(fixURL(url1))
        TargetNamespace(doc1.targetNamespace)
        Imports{
          doc1.imports.each { imp ->
            Import(imp.namespace)
          }
        }
      }
      "Modified"{
        URL(fixURL(url1))
        TargetNamespace(doc2.targetNamespace)
        Imports{
          doc2.imports.each { imp ->
            Import(imp.namespace)
          }
        }
      }
      Diffs{
        diffs.each{ diff -> dump(diff) }
      }
    }
//    def input = new ByteArrayInputStream(writer.toByteArray())
//    transform(input, 'html')
		new File(reportFolder).mkdir()
		File xml = new File("$reportFolder/diff-report.xml")
		OutputStream outputStream = new FileOutputStream (xml);
		writer.writeTo(outputStream);
		transform(new ByteArrayInputStream(writer.toByteArray()), 'html')
  }

  public String getCliUsage() {
    'schemadiff <first-schema> <second-schema> [report directory]'
  }

  public getParser() {
    new SchemaParser()
  }
  
  public getStylesheet(format) {
    "${System.getenv('SOA_MODEL_HOME')}/src/main/style/schema2"+format+".xsl"
  }

  public getDiffGenerator(doc1, doc2) {
    new SchemaDiffGenerator(doc1, doc2)
  }
}
