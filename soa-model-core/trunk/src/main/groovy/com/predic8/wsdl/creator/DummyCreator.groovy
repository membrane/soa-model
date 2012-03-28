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

class DummyCreator extends AbstractWSDLCreator{  
  
  def createDefinitions(definitions, context){
    println "createDefinitions"
  }
  
  def createMessage(message, context){
    println "createMessage"
  }
  
  def createPart(part, context){
    println "createPart"
  }
  
  def createPortType(portType, context){
    println "createPortType"
  }
  
  def createOperation(operation, context){
    println "createOperation"
  }
  
  def createBinding(binding, context){
    println "createBinding"
  }
  
  def createSoapBinding(binding, context){
    println "createSoapBinding"
  }
  
  def createBindingOperation(operation, context){
    println "createBindingOperation"
  }
  
  def createService(service, context){
    println "createService"
  }
  
  def createPort(port, context){
    println "createPort"
  }
}
