package com.predic8.wstool.creator

import groovy.xml.MarkupBuilder

import com.predic8.schema.*
import com.predic8.schema.restriction.StringRestriction;
import com.predic8.wsdl.*
import com.predic8.wsdl.creator.WSDLCreator
import com.predic8.wsdl.creator.WSDLCreatorContext
import static com.predic8.schema.Schema.*;

class SOARequestCreatorWithCreatedWSDLTest extends GroovyTestCase{

  Definitions wsdl

  void setUp(){
    WSDLParser parser = new WSDLParser()
    wsdl = parser.parse("http://www.thomas-bayer.com/axis2/services/BLZService?wsdl")
    addOperation()
  }

  private void addOperation(){
    Schema schema = wsdl.getSchema("http://thomas-bayer.com/blz/")
    schema.newElement("listBanks").newComplexType().newSequence()
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
  }

  void testDefinitions(){
    assertEquals(2, wsdl.getBinding('BLZServiceSOAP11Binding').operations.size())
  }
  
  void testSOARequest(){
    StringWriter writer = new StringWriter()
    SOARequestCreator creator = new SOARequestCreator(wsdl, new RequestTemplateCreator(), new MarkupBuilder(writer))
    creator.createRequest("BLZServicePortType", "listBanks", "BLZServiceSOAP11Binding")
    assertEquals('listBanks', new XmlSlurper().parseText(writer.toString()).Envelope.body.listBanks.name())
  }
  
}
