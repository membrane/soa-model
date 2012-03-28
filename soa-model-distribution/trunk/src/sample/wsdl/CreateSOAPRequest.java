package sample.wsdl;

import groovy.xml.MarkupBuilder;
import java.io.StringWriter;
import java.util.HashMap;

import com.predic8.wsdl.*;
import com.predic8.wstool.creator.*;

public class CreateSOAPRequest {

  public static void main(String[] args) {

    WSDLParser parser = new WSDLParser();
    
    Definitions wsdl = parser.parse("resources/article/article.wsdl");
    
    StringWriter writer = new StringWriter();
    
    HashMap<String, String> formParams = new HashMap<String, String>();
    formParams.put("xpath:/create/article/name", "foo");
    formParams.put("xpath:/create/article/description", "bar");
    formParams.put("xpath:/create/article/price/amount", "00.00");
    formParams.put("xpath:/create/article/price/currency", "USD");
    formParams.put("xpath:/create/article/id", "1");
    
    SOARequestCreator creator = new SOARequestCreator();
    creator.setBuilder(new MarkupBuilder(writer));
    creator.setDefinitions(wsdl);
    creator.setFormParams(formParams);
    creator.setCreator(new RequestCreator());
    
    //creator.createRequest(PortType name, Operation name, Binding name);
    creator.createRequest("ArticleServicePT", "create", "ArticleServicePTBinding");

    System.out.println(writer);
  }
}