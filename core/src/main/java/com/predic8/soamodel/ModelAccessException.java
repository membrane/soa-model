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

package com.predic8.soamodel;

import com.predic8.schema.SchemaComponent;
import com.predic8.wsdl.WSDLElement;

public class ModelAccessException extends RuntimeException {

	protected WSDLElement wsdlElement;
	protected SchemaComponent schemaComponent;

	private static final long serialVersionUID = 5405085246506357279L;

	public ModelAccessException() {
		super();
	}

	public ModelAccessException(String message, Throwable cause,
	    boolean enableSuppression, boolean writableStackTrace) {
	//	super(message, cause, enableSuppression, writableStackTrace);
		super(message, cause);
	}

	public ModelAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModelAccessException(Throwable cause) {
		super(cause);
	}

	public ModelAccessException(String message) {
		super(message);
	}
	
	public ModelAccessException(String message, Throwable cause,
			WSDLElement wsdlElement) {
		super(message, cause);
		this.wsdlElement = wsdlElement;
	}
	
	public ModelAccessException(String message, WSDLElement wsdlElement) {
		super(message);
		this.wsdlElement = wsdlElement;
	}
	
	public ModelAccessException(String message, Throwable cause,
			SchemaComponent schemaComponent) {
		super(message, cause);
		this.schemaComponent = schemaComponent;
	}
	
	public ModelAccessException(String message, SchemaComponent schemaComponent) {
		super(message);
		this.schemaComponent = schemaComponent;
	}

	public WSDLElement getElement() {
		return wsdlElement;
	}

	public void setElement(WSDLElement element) {
		this.wsdlElement = element;
	}
	
	public SchemaComponent getSchemaComponent() {
		return schemaComponent;
	}

	public void setSchemaComponent(SchemaComponent schemaComponent) {
		this.schemaComponent = schemaComponent;
	}

}
