package com.predic8.soamodel

import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.PortType;
import com.predic8.wsdl.WSDLParser
import com.predic8.wsdl.WSDLVersion2NotSupportedException;
import com.predic8.xml.util.ClasspathResolver

class ModelAccessExceptionsTest extends GroovyTestCase {
	
	Definitions wsdl
	
	protected void setUp() throws Exception {
		def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
		wsdl = parser.parse('BLZServiceWithException.wsdl')
	}
	
	void testMissingMessage() {
	  assert !wsdl.operations[0].input.message
		shouldFail(MessageAccessException) { 
			wsdl.bindings[0].operations[0].input.message
	  }
		try {
			wsdl.bindings[0].operations[0].input.message
		} catch (Exception e) {
			assert "Could not find the message 'tns:getBank', used in the input of the operation 'getBank'." == e.message
			assert e.wsdlElement instanceof Operation
			assert e.messageName == 'tns:getBank'
		}
  }
	
	void testMissingOperation(){
		shouldFail(OperationAccessException) {
			wsdl.getBinding('BLZServiceSOAP11Binding').operations[1].input.message
		}
		try {
			wsdl.getBinding('BLZServiceSOAP11Binding').operations[1].input.message
		} catch (Exception e) {
			assert "Could not find the matching operation for 'TestOperation' in the portType 'BLZServicePortType'." == e.message
			assert e.wsdlElement instanceof PortType
		}
	}
	
}
