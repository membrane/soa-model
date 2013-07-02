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

import com.predic8.soamodel.AbstractCreator
import com.predic8.wsdl.*

abstract class AbstractWSDLCreator extends AbstractCreator{
  
  def abstract createDefinitions(Definitions definitions, WSDLCreatorContext context)
  
  def abstract createImport(Import imp, WSDLCreatorContext context)
	
  def abstract createTypes(Types types, WSDLCreatorContext context)
  
  def abstract createMessage(Message message, WSDLCreatorContext context)
  
  def abstract createPart(Part part, WSDLCreatorContext context)
  
  def abstract createPortType(PortType portType, WSDLCreatorContext context)
  
  def abstract createOperation(Operation operation, WSDLCreatorContext context)
  
  def abstract createBinding(Binding binding, WSDLCreatorContext context)
  
  def abstract createSoapBinding(AbstractSOAPBinding binding, WSDLCreatorContext context)
  
  def abstract createBindingOperation(BindingOperation operation, WSDLCreatorContext context)
  
  def abstract createService(Service service, WSDLCreatorContext context)
  
  def abstract createPort(Port port, WSDLCreatorContext context)
  
}
