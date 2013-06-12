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

package samples.wsdl;

import groovy.xml.MarkupBuilder;
import java.io.StringWriter;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;
import com.predic8.wstool.creator.RequestTemplateCreator;
import com.predic8.wstool.creator.SOARequestCreator;

public class CreateSOAPRequestTemplate {

  public static void main(String[] args) {

    WSDLParser parser = new WSDLParser();
    
    Definitions wsdl = parser.parse("samples/wsdl/article-service/article.wsdl");
    
    StringWriter writer = new StringWriter();
    
    //SOAPRequestCreator constructor: SOARequestCreator(Definitions, Creator, MarkupBuilder)
    SOARequestCreator creator = new SOARequestCreator(wsdl, new RequestTemplateCreator(), new MarkupBuilder(writer));
    
    //creator.createRequest(PortType name, Operation name, Binding name);
    creator.createRequest("ArticleServicePT", "create", "ArticleServicePTBinding");

    System.out.println(writer);
  }
}