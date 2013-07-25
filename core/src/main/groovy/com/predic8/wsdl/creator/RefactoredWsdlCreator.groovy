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
 
package com.predic8.wsdl.creator

import com.predic8.wsdl.*

class RefactoredWsdlCreator extends AbstractWSDLCreator {
    @Override
    Object createDefinitions(Definitions definitions, WSDLCreatorContext wsdlCreatorContext) {
        definitions.documentation?.create(this, wsdlCreatorContext)

        ['types', 'messages', 'portTypes', 'bindings', 'services'].each {
            definitions."$it".each {
                it.create(this, wsdlCreatorContext)
            }
        }
    }

    @Override
    Object createImport(Import anImport, WSDLCreatorContext wsdlCreatorContext) {
        return null  // TODO implement me
    }

    @Override
    Object createTypes(Types types, WSDLCreatorContext wsdlCreatorContext) {
        types.documentation?.create(this, wsdlCreatorContext)
        // TODO implement me
//        types.schemas.each {
//            it.create(new SchemaCreator(builder: builder), new SchemaCreatorContext(wsdlCreatorContext.creatorContext))
//        }
    }

    @Override
    Object createMessage(Message message, WSDLCreatorContext wsdlCreatorContext) {
        message.documentation?.create(this, wsdlCreatorContext)
        message.parts.each {
            it.create(this, wsdlCreatorContext)
        }
    }

    @Override
    Object createPart(Part part, WSDLCreatorContext wsdlCreatorContext) {
        return null  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    Object createPortType(PortType portType, WSDLCreatorContext wsdlCreatorContext) {
        portType.documentation?.create(this, wsdlCreatorContext)
        portType.operations.each {
            it.create(this, wsdlCreatorContext)
        }
    }

    @Override
    Object createOperation(Operation operation, WSDLCreatorContext wsdlCreatorContext) {
        operation.with {
            documentation?.create(this, wsdlCreatorContext)
            input.create(this, wsdlCreatorContext)
            output?.create(this, wsdlCreatorContext)
            faults.each {
                it.create(this, wsdlCreatorContext)
            }
        }
    }

    @Override
    Object createBinding(com.predic8.wsdl.Binding binding, WSDLCreatorContext wsdlCreatorContext) {
        binding.documentation?.create(this, wsdlCreatorContext)
        binding.binding?.create(this, wsdlCreatorContext)
        binding.operations.each {
            it.parent = binding
            it.create(this, wsdlCreatorContext)
        }
    }

    @Override
    Object createSoapBinding(AbstractSOAPBinding abstractSOAPBinding, WSDLCreatorContext wsdlCreatorContext) {
        // TODO implement me
    }

    @Override
    Object createBindingOperation(BindingOperation bindingOperation, WSDLCreatorContext wsdlCreatorContext) {
        bindingOperation.with {
            operation?.create(this, wsdlCreatorContext)
            input?.create(this, wsdlCreatorContext)
            output?.create(this, wsdlCreatorContext)
            faults.each {
                it.create(this, wsdlCreatorContext)
            }
        }

    }

    @Override
    Object createService(com.predic8.wsdl.Service service, WSDLCreatorContext wsdlCreatorContext) {
        service.documentation?.create(this, wsdlCreatorContext)
        service.ports.each {
            it.create(this, wsdlCreatorContext)
        }
    }

    @Override
    Object createPort(Port port, WSDLCreatorContext wsdlCreatorContext) {
        port.documentation?.create(this, wsdlCreatorContext)
        port.address.create(this, wsdlCreatorContext)
    }

    Object createBindingMessage(BindingMessage bindingMessage, WSDLCreatorContext wsdlCreatorContext) {
        bindingMessage.bindingElements.each {
            it.create(this, wsdlCreatorContext)
        }
    }

    Object createPortTypeMessage(AbstractPortTypeMessage portTypeMessage, WSDLCreatorContext wsdlCreatorContext) {

    }

    Object createSOAPBody(AbstractSOAPBody abstractSOAPBody, WSDLCreatorContext wsdlCreatorContext) {

    }

    Object createAddress(AbstractAddress abstractAddress, WSDLCreatorContext wsdlCreatorContext) {

    }

    Object createDocumentation(Documentation documentation, WSDLCreatorContext wsdlCreatorContext) {

    }
}