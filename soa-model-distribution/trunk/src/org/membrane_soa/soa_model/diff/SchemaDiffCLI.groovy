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
      "Schema-a"{
        URL(url1)
        TargetNamespace(doc1.targetNamespace)
        Imports{
          doc1.imports.each { imp ->
            Import(imp.namespace)
          }
        }
      }
      "Schema-b"{
        URL(url2)
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
    def input = new ByteArrayInputStream(writer.toByteArray())
    transform(input)
  }

  public String getCliUsage() {
    'SchemaDiff -a <first document> -b <second document>'
  }

  public getParser() {
    new SchemaParser()
  }
  
  public getStylesheet() {
    "${System.getenv('SOA_MODEL_HOME')}/style/schema/2html.xslt"
  }

  public getDiffGenerator(doc1, doc2) {
    new SchemaDiffGenerator(doc1, doc2)
  }
}
