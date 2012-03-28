package sample.wsdl;

import com.predic8.wsdl.*;

public class ListWSDLPorts {

  public static void main(String[] args) {
    WSDLParser parser = new WSDLParser();

    Definitions wsdl = parser.parse("http://www.thomas-bayer.com/axis2/services/BLZService?wsdl");
    
    for(Service service : wsdl.getServices()){
      System.out.println(service.getName());
      for(Port port : service.getPorts()){
        System.out.println(port.getName() + "( " + port.getBinding().getName()+", " + port.getAddress().getLocation() + " )");
      }
    }
    
    System.out.println(wsdl.getAsString());
  }
}