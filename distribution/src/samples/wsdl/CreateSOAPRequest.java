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

package wsdl;

import groovy.xml.MarkupBuilder;

import java.io.StringWriter;
import java.util.HashMap;

import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;
import com.predic8.wstool.creator.RequestCreator;
import com.predic8.wstool.creator.SOARequestCreator;

public class CreateSOAPRequest {

  public static void main(String[] args) {

    WSDLParser parser = new WSDLParser();
    
    Definitions wsdl = parser.parse("samples/article/article.wsdl");
    
    StringWriter writer = new StringWriter();
    
    HashMap<String, String> formParams = new HashMap<String, String>();
    formParams.put("xpath:/create/article/name", "foo");
    formParams.put("xpath:/create/article/description", "bar");
    formParams.put("xpath:/create/article/price/amount", "00.00");
    formParams.put("xpath:/create/article/price/currency", "USD");
    formParams.put("xpath:/create/article/id", "1");
    
    //SOARequestCreator constructor: SOARequestCreator(Definitions, Creator, MarkupBuilder)
    SOARequestCreator creator = new SOARequestCreator(wsdl, new RequestCreator(), new MarkupBuilder(writer));
    creator.setFormParams(formParams);
    
    //creator.createRequest(PortType name, Operation name, Binding name);
    creator.createRequest("ArticleServicePT", "create", "ArticleServicePTBinding");

    System.out.println(writer);
  }
}