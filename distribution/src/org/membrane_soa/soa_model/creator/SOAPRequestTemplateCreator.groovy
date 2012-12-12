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

package org.membrane_soa.soa_model.creator;

import groovy.xml.MarkupBuilder;
import java.io.StringWriter;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;
import com.predic8.wstool.creator.RequestTemplateCreator;
import com.predic8.wstool.creator.SOARequestCreator;

public class CreateSOAPRequestTemplate {
  
  Definitions wsdl
  String url
  String output
  def builder

  public static void main(String[] args) {
    CreateSOAPRequestTemplate csrt = new CreateSOAPRequestTemplate()
    csrt.start(args)
  }
  
  def start(args){
    setUp(args)
    StringWriter writer = new StringWriter()
    SOARequestCreator creator = new SOARequestCreator(wsdl, new RequestTemplateCreator(), new MarkupBuilder(writer))
    //creator.createRequest(PortType name, Operation name, Binding name);
    def s11Bindings = wsdl.getBindings('SOAP11')
    s11Bindings.each {
      println it.name
      println it.portType
    }
//    creator.createRequest("ArticleServicePT", "create", "ArticleServicePTBinding")
//    System.out.println(writer)
  }
  
  public void setUp(args){
    def cli = new CliBuilder()
    cli.usage = 'createRequest -f <document file name>'
    cli.h('Show help')
    cli.f('Give the file name of the WSDL document.' , args:1)
    cli.u('Give the URL of the WSDL document.' , args:1)
    cli.o('Directory where the request is written. If not set, the result will apear here.', args: 1)

    def options = cli.parse(args)
    
    if(options.f || options.u) {
      WSDLParser parser = new WSDLParser()
      try {
        wsdl = parser.parse(options.f ?: options.u)
      } catch (IOException e) {
        println "Can not parse the document from: ${options.f ?: options.u}"
        System.exit(1)
      }
      output = options.o ?: 'output'
    }
    else {
      cli.usage()
      System.exit(1)
    }
  }

}