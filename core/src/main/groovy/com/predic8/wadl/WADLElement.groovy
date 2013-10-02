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

package com.predic8.wadl

import java.util.List;

import javax.xml.namespace.QName;

import com.predic8.soamodel.XMLElement;

abstract class WADLElement extends XMLElement {
	
	Application application
	List<Doc> docs = []
	WADLElement parent
	
	protected parseChildren(token, child, ctx) {
		switch (token.name) {
			case Doc.ELEMENTNAME :
				def doc = new Doc()
				doc.parse(token, ctx)
				docs << doc
				break
		}
	}

	public QName getElementName() {
		ELEMENTNAME
	}
	
  public String getNamespaceUri() {
	  // TODO Auto-generated method stub
	  return null;
  }
	
	public String getFullPath() {
		parent.fullPath
	}

	@Override
  public String getPrefix() {
	  // TODO Auto-generated method stub
	  return null;
  }
	
}