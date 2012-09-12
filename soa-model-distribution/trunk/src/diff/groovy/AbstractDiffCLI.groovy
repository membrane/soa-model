package diff.groovy

import com.predic8.soamodel.Difference;
import groovy.util.CliBuilder;

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
      output = options.o ?: ''
      format = options.f ?: 'xml'
    }
    else {
      cli.usage()
      System.exit(1)
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
