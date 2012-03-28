package sample.wsdl;

import com.predic8.wsdl.*;
import com.predic8.schema.*;
import static com.predic8.schema.Schema.*;

public class ManipulateWSDL {

  public static void main(String[] args) {
    WSDLParser parser = new WSDLParser();

    Definitions wsdl = parser.parse("http://www.thomas-bayer.com/axis2/services/BLZService?wsdl");
    
    Schema schema = wsdl.getSchema("http://thomas-bayer.com/blz/");
    schema.newElement("listBanks").newComplexType().newSequence();
    Sequence seq = schema.newElement("listBanksResponse").newComplexType().newSequence();
    seq.newElement("bank", STRING);
    seq.newElement("name", STRING);
    
    PortType pt = wsdl.getPortType("BLZServicePortType");
    Operation op = pt.newOperation("listBanks");
    op.newInput("listBanks").newMessage("listBanks").newPart("parameters", "tns:listBanks");
    op.newOutput("listBanksResponse").newMessage("listBanksResponse").newPart("parameters", "tns:listBanksResponse");
    
    Binding bnd = wsdl.getBinding("BLZServiceSOAP11Binding");
    BindingOperation bo = bnd.newBindingOperation("listBanks");
    bo.newSOAP11Operation();
    bo.newInput().newSOAP11Body();
    bo.newOutput().newSOAP11Body();
    
    System.out.println(wsdl.getAsString());
  }
}