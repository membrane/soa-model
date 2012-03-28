package sample.wsdl;

import com.predic8.wsdl.*;

public class ListWSDLOperations {

  public static void main(String[] args) {
    WSDLParser parser = new WSDLParser();

    Definitions defs = parser
        .parse("http://ws.xwebservices.com/XWebBlog/V2/XWebBlog.wsdl");

    for (PortType pt : defs.getPortTypes()) {
      System.out.println(pt.getName());
      for (Operation op : pt.getOperations()) {
        System.out.println(" -" + op.getName());
      }
    }
  }
}
