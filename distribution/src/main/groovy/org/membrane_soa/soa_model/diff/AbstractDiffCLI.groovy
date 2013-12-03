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
import com.predic8.xml.util.ResourceDownloadException;

abstract class AbstractDiffCLI {

  def doc1
  def doc2
  String url1
  String url2
	List<Difference> diffs
  String reportFolder
  def builder

  public start(args){
    setUp(args)
    diffs = getDiffGenerator(doc1, doc2).compare()
    Diff2Xml(diffs)
		System.out.println("Report generated in $reportFolder");
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
        System.err.println("Can not parse the document from: ${url1}")
        System.err.println(e)
        System.exit(1)
      } catch (ResourceDownloadException e) {
				System.err.println("Can not parse the document from ${url1}")
				System.err.println("Can not get resource from ${e.url}")
				System.exit(1)
			}
			 
      try {
        doc2 = parser.parse(url2)
      } catch (IOException e) {
        System.err.println("Can not parse the document from: ${url2}")
        System.err.println(e)
        System.exit(1)
      } catch (ResourceDownloadException e) {
				System.err.println("Can not parse the document from ${url2}")
				System.err.println("Can not get resource from ${e.url}")
				System.exit(1)
			}
      if(options.getArgs().size() > 2) reportFolder = options.getArgs()[2]
      else reportFolder = 'diff-report'
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
      Source inputXML  = new StreamSource(input)
			
			new File("$reportFolder/web").mkdirs()
			Result result = new StreamResult(new FileWriter("$reportFolder/diff-report.$format"))
			stylesheet.transform(inputXML, result)

			copy("${System.getenv('SOA_MODEL_HOME')}/src/main/web","$reportFolder/web")
    }
    catch (TransformerException e) {
      System.err.println(e);
    }
  }

  def copy(from, to) {
    new File(from).listFiles().each { entry ->
      if(entry.name.contains('svn')) return

      if(entry.isDirectory()) {
        new File("$to/${entry.name}").mkdir()
        copy("$from/${entry.name}", "$to/${entry.name}")
        return
      }

      new File("$to/${entry.name}").withOutputStream { out ->
        new File("$from/${entry.name}").withDataInputStream { dataIn ->
          out << dataIn
        }
      }
    }
  }

  def dump(diff) {
    builder.Diff(safe:diff.safe, type:diff.type, breaks:diff.breaks, exchange:diff.exchange() ){
      Description("$diff.description")
      diff.diffs.each{ dump(it) }
    }
  }
	
	String fixURL(String url) {
		if(url.startsWith('http')) return url
		new File(url).getAbsoluteFile()
	}

  abstract String getCliUsage()

  abstract getParser()
  
  abstract getDiffGenerator(doc1, doc2)

  abstract void Diff2Xml(List<Difference> diffs)
}
