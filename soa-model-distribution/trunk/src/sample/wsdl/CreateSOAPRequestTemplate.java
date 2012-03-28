package sample.wsdl;

import groovy.xml.MarkupBuilder;
import java.io.StringWriter;

import com.predic8.wsdl.*;
import com.predic8.wstool.creator.*;

public class CreateSOAPRequestTemplate {

  public static void main(String[] args) {

    WSDLParser parser = new WSDLParser();
    
    Definitions wsdl = parser.parse("resources/article/article.wsdl");
    
    StringWriter writer = new StringWriter();
    SOARequestCreator creator = new SOARequestCreator();
    creator.setBuilder(new MarkupBuilder(writer));
    creator.setDefinitions(wsdl);
    creator.setCreator(new RequestTemplateCreator());
    
    //creator.createRequest(PortType name, Operation name, Binding name);
    creator.createRequest("ArticleServicePT", "create", "ArticleServicePTBinding");

    System.out.println(writer);
  }
}