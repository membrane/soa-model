package diff.groovy


import groovy.xml.MarkupBuilder
import javax.xml.transform.*
//import javax.xml.transform.Result
//import javax.xml.transform.Source
//import javax.xml.transform.Transformer
//import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource
import com.predic8.schema.SchemaParser;
import com.predic8.schema.diff.SchemaDiffGenerator;
import com.predic8.soamodel.Difference;

class SchemaDiffCLI extends AbstractDiffCLI{

  public static void main(String[] args) {
    SchemaDiffCLI diffCLI = new SchemaDiffCLI().start(args)
  }

  void Diff2Xml(List<Difference> diffs){
    new File("output").mkdir();
//    def writer = new FileWriter("output/raw.xml")
    def writer = new ByteArrayOutputStream()
    builder = new MarkupBuilder(new PrintWriter(writer))
//    if(output){
//      new File(output).mkdir();
//      builder = new MarkupBuilder(new FileWriter("$output/schemaDiff.xml"))
//    }
//    else{
//      builder = new MarkupBuilder()
//    }
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
  
  public transform(input){
    try {
      TransformerFactory xformFactory = TransformerFactory.newInstance()
      Source xsl = new StreamSource("style/copy.xslt")
      Transformer stylesheet = xformFactory.newTransformer(xsl)
    
      Source request  = new StreamSource(input)
      Result response = new StreamResult(new FileWriter("output/result.xml"))
      stylesheet.transform(request, response)
    }
    catch (TransformerException e) {
      System.err.println(e);
    }
  }

  public String getCliUsage() {
    'SchemaDiff -a <first document> -b <second document>'
  }

  public getParser() {
    new SchemaParser()
  }

  public getDiffGenerator(doc1, doc2) {
    new SchemaDiffGenerator(doc1, doc2)
  }
}
