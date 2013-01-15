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
  String reportFolder
  def builder

  public start(args){
    setUp(args)
    List<Difference> lst = getDiffGenerator(doc1, doc2).compare()
    Diff2Xml(lst)
  }
  
  public void setUp(args){
    def cli = new CliBuilder()
    cli.usage = getCliUsage()

    def options = cli.parse(args)
    
    if(options.getArgs().size() >= 2){
      url1= options.getArgs()[0]
      url2= options.getArgs()[1]
      def parser = getParser()
      try {
        doc1 = parser.parse(url1)
      } catch (IOException e) {
        println "Can not parse the document from: ${url1}"
		cli.usage()
        System.exit(1)
      }
      try {
        doc2 = parser.parse(url2)
      } catch (IOException e) {
		println "Can not parse the document from: ${url2}\n"
		cli.usage()
        System.exit(1)
      }
      if(options.getArgs().size() > 2) reportFolder = options.getArgs()[2]
      else reportFolder = 'report'
    }
    else {
      cli.usage()
      System.exit(1)
    }
  }
  
  public transform(ByteArrayInputStream input, String format){
    try {
      TransformerFactory xformFactory = TransformerFactory.newInstance()
      Source xsl = new StreamSource(getStylesheet(format))
      Transformer stylesheet = xformFactory.newTransformer(xsl)
      Source request  = new StreamSource(input)

      Result response = new StreamResult(new FileWriter("$reportFolder/diff-report.$format"))
      stylesheet.transform(request, response)
      
      new File("$reportFolder/static").mkdir()
      copy("${System.getenv('SOA_MODEL_HOME')}/static","$reportFolder/static")
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
