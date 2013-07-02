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

package com.predic8.wsdl.creator;

import com.predic8.wsdl.*

class DummyCreator extends AbstractWSDLCreator{  
  
  def createDefinitions(Definitions definitions, WSDLCreatorContext ctx){
    println "createDefinitions"
  }

	def createImport(Import imp, WSDLCreatorContext context){
		println "createImport"
	}
	
	
  def createTypes(Types types, WSDLCreatorContext context) {
		println "createTypes"
	}
  
  def createMessage(Message message, WSDLCreatorContext ctx){
    println "createMessage"
  }
  
  def createPart(Part part, WSDLCreatorContext ctx){
    println "createPart"
  }
  
  def createPortType(PortType portType, WSDLCreatorContext ctx){
    println "createPortType"
  }
  
  def createOperation(Operation operation, WSDLCreatorContext ctx){
    println "createOperation"
  }
  
  def createBinding(Binding binding, WSDLCreatorContext ctx){
    println "createBinding"
  }
  
  def createSoapBinding(AbstractSOAPBinding binding, WSDLCreatorContext ctx){
    println "createSoapBinding"
  }
  
  def createBindingOperation(BindingOperation operation, WSDLCreatorContext ctx){
    println "createBindingOperation"
  }
  
  def createService(Service service, WSDLCreatorContext ctx){
    println "createService"
  }
  
  def createPort(Port port, WSDLCreatorContext ctx){
    println "createPort"
  }
}
