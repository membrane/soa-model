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

import com.predic8.xml.util.PrefixedName;

public class NamespaceNotDeclaredForReferenceException extends RuntimeException {
	
  private static final long serialVersionUID = -2200072011898720523L;

	public NamespaceNotDeclaredForReferenceException(String message, Object reference, XMLElement element) { 
		super(message);
		setReference((PrefixedName) reference);
		setElement(element);
	}

	PrefixedName reference;
	XMLElement element;

	public PrefixedName getReference() {
		return reference;
	}

	private void setReference(PrefixedName reference) {
		this.reference = reference;
	}

	private void setElement(XMLElement element) {
		this.element = element;
	}
}
