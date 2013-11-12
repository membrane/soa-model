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

import groovy.xml.QName;

public class ElementRefAccessException extends ModelAccessException {

	private static final long serialVersionUID = 5405085246506357279L;

	private QName ref;
	private String prefix;

	public ElementRefAccessException(String message, QName ref, String prefix) {
	  super(message);
	  this.setRef(ref);
	  this.setPrefix(prefix);
  }

	public QName getRef() {
	  return ref;
  }

	public void setRef(QName ref) {
	  this.ref = ref;
  }

	public String getPrefix() {
	  return prefix;
  }

	public void setPrefix(String prefix) {
	  this.prefix = prefix;
  }

}
