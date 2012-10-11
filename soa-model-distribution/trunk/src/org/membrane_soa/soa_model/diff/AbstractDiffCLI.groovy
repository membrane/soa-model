package org.membrane_soa.soa_model.diff

import javax.xml.transform.Result
import javax.xml.transform.Source
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

import com.predic8.soamodel.Difference

abstract class AbstractDiffCLI {

  def doc1
  def doc2
  String url1
  String url2
  String output
  String format
  def builder

  public start(args){
    setUp(args)
    List<Difference> lst = getDiffGenerator(doc1, doc2).compare()
    Diff2Xml(lst)
    if(format) {
//      transform(new File('output/raw.xml'))
    }
  }
  
  public void setUp(args){
    def cli = new CliBuilder()
    cli.usage = getCliUsage()
    cli.h('Show help')
    cli.a('The first document to compare' , args:1)
    cli.b('The second document to compare' , args:1)
    cli.o('Directory where the report is written. If not set, the result will apear here.', args: 1)
    cli.f('Format of the output(xml, html, txt)', args:1)

    def options = cli.parse(args)
    
    if(options.a && options.b) {
      url1 = options.a
      url2 = options.b
      def parser = getParser()
      try {
        doc1 = parser.parse(url1)
      } catch (IOException e) {
        println "Can not parse the document from: ${url1}"
        System.exit(1)
      }
      try {
        doc2 = parser.parse(url2)
      } catch (IOException e) {
        println "Can not parse the document from: ${url2}"
        System.exit(1)
      }
      output = options.o ?: 'report'
      format = options.f ?: 'xml'
    }
    else {
      cli.usage()
      System.exit(1)
    }
  }
  
  public transform(input){
    try {
      TransformerFactory xformFactory = TransformerFactory.newInstance()
      Source xsl = new StreamSource(stylesheet)
      Transformer stylesheet = xformFactory.newTransformer(xsl)
      Source request  = new StreamSource(input)

      new File(output).mkdir()
      Result response = new StreamResult(new FileWriter("$output/index.htm"))
      stylesheet.transform(request, response)
      
      new File("$output/static").mkdir()
      copy('static',"$output/static")
    }
    catch (TransformerException e) {
      System.err.println(e);
    }
  }
  
  def copy(from, to) {
    new File(from).listFiles().each { entry ->
      if(entry.name.contains('svn')) return
      if (entry.isDirectory()) {
        new File("$to/${entry.name}").mkdir()
        copy("$from/${entry.name}", "$to/${entry.name}")
        return
      }
      new File("$to/${entry.name}").newOutputStream() << new File("$from/${entry.name}").newDataInputStream()
    }
  }

  def dump(diff) {
    builder.Diff(safe:diff.safe, type:diff.type, breaks:diff.breaks ){
      Description("$diff.description")
      diff.diffs.each{ dump(it) }
    }
  }

  abstract String getCliUsage()

  abstract getParser()
  
  abstract getDiffGenerator(doc1, doc2)

  abstract void Diff2Xml(List<Difference> diffs)
}
