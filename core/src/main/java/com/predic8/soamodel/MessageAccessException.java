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

import com.predic8.wsdl.WSDLElement;

public class MessageAccessException extends RuntimeException {

	private static final long serialVersionUID = -4189699847233028273L;
	
	private WSDLElement wsdlElement;

	public MessageAccessException() {
		super();
	}

	public MessageAccessException(String message, Throwable cause,
	    boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MessageAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageAccessException(Throwable cause) {
		super(cause);
	}

	public MessageAccessException(String message) {
		super(message);
	}
	
	public MessageAccessException(String message, Throwable cause,
			WSDLElement wsdlElement) {
		super(message, cause);
		this.wsdlElement = wsdlElement;
	}
	
	public MessageAccessException(String message, WSDLElement wsdlElement) {
		super(message);
		this.wsdlElement = wsdlElement;
	}

	public WSDLElement getElement() {
		return wsdlElement;
	}

	public void setElement(WSDLElement element) {
		this.wsdlElement = element;
	}

}
