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

public class PortTypeAccessException extends ModelAccessException {

  private static final long serialVersionUID = -5049330036052285461L;
  
	public PortTypeAccessException() {
		super();
	}

	public PortTypeAccessException(String message, Throwable cause,
	    boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PortTypeAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public PortTypeAccessException(Throwable cause) {
		super(cause);
	}

	public PortTypeAccessException(String message) {
		super(message);
	}
	
	public PortTypeAccessException(String message, Throwable cause,
			WSDLElement wsdlElement) {
		super(message, cause);
		this.wsdlElement = wsdlElement;
	}
	
	public PortTypeAccessException(String message, WSDLElement wsdlElement) {
		super(message);
		this.wsdlElement = wsdlElement;
	}

}
