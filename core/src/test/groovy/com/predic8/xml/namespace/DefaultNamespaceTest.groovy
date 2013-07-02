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

package com.predic8.xml.namespace

import junit.framework.TestCase

import javax.xml.stream.*

import com.predic8.soamodel.*

class A2 extends XMLElement {
	static final String NAMESPACE = ''
  def b
  def c
  
  protected parseChildren(token, child, params) {
    switch ( child ) {
      case 'b' :
      b = new B2()
      b.parse(token,params)
      break
    }
    switch ( child ) {
      case 'c' :
      c = new C2()
      c.parse(token,params)
      break
    }
  }  
  def getElementName() {
    'a'
  }
  public String getNamespaceUri() {
	  ''
  }
}

class B2 extends XMLElement {
	static final String NAMESPACE = ''
  def getElementName() {
    'b'
  }
  public String getNamespaceUri() {
	  ''
  }
}

class C2 extends XMLElement {
	static final String NAMESPACE = 'uri:c'
  def d
  def e
  
  protected parseChildren(token, child, params) {
    switch ( child ) {
      case 'd' :
      d = new D2()
      d.parse(token,params)
      break
      case 'e' :
      e = new E2()
      e.parse(token,params)
      break
    }
  }
  def getElementName() {
    'c'
  }
	
	public String getNamespaceUri() {
		'uri:c'
	}
}

class D2 extends XMLElement {
	static final String NAMESPACE = ''
  def getElementName() {
    'd'
  }
	
	public String getNamespaceUri() {
		''
	}
}

class E2 extends XMLElement {
	static final String NAMESPACE = 'uri:e'
  def getElementName() {
    'e'
  }
	
	public String getNamespaceUri() {
		'uri:e'
	}
}


class DefaultNamespaceTest extends GroovyTestCase {
  def a
  
  void setUp() {
    def token = XMLInputFactory.newInstance().createXMLStreamReader(this.class.getResourceAsStream("/namespaces/defaultNS.xml"))    
    a = new A2()
    token.nextTag()
    a.parse(token, [:])
  }
	
  void testA(){    
    assertEquals('',a.getPrefix(""))
    assertEquals('',a.getNamespace(""))
  }
  
  void testC() {    
    assertEquals('',a.c.getPrefix("uri:c"))
    assertEquals('uri:c',a.c.getNamespace(""))
  }

  void testD() {  
    assertEquals('',a.c.d.getPrefix(""))
    assertEquals('',a.c.d.getNamespace(""))
  }
  
  void testE() {
    assertEquals('ns1',a.c.e.getPrefix("uri:e"))
    assertEquals('uri:e',a.c.e.getNamespace("ns1"))
  }
  
}
