package sample.wsdl;


import com.predic8.wsdl.*;
import com.predic8.schema.*;
import static com.predic8.schema.Schema.*;

public class GenerateAbstractWSDL {

  public static void main(String[] args) {

    Schema schema = new Schema("http://predic8.com/add/1/");
    schema.newElement("add").newComplexType().newSequence()
        .newElement("summand", INT).setMaxOccurs("unbounded");
    schema.newElement("addResponse").newComplexType().newSequence()
        .newElement("number", INT);

    Definitions wsdl = new Definitions("http://predic8.com/wsdl/AddService/1/","AddService");
    wsdl.add(schema);

    PortType pt = wsdl.newPortType("AddPortType");
    Operation op = pt.newOperation("add");

    op.newInput("add").newMessage("add").newPart("parameters", "tns:add");
    op.newOutput("addResponse").newMessage("addResponse")
        .newPart("parameters", "tns:addResponse");
    
    System.out.println(wsdl.getAsString());
  }
}