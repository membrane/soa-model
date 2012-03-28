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

package com.predic8.xml.util

import junit.framework.TestCase

class QNameTest extends GroovyTestCase {
  
  def static pn1 = new PrefixedName("tns:BLZServiceSOAP11Binding")
  def static pn2 = new PrefixedName("tns", "BLZServiceSOAP11Binding")
  def static pn3 = new PrefixedName('onlyLocal')
  
  void testQName1(){
    assertEquals('tns' , pn1.prefix)
    assertEquals('BLZServiceSOAP11Binding' , pn1.localName)
  }
  
  void testQName2(){
    assertEquals('tns' , pn2.prefix)
    assertEquals('BLZServiceSOAP11Binding' , pn2.localName)
  }
  
  void testInitialllized() {
    assertNotNull(pn3.prefix)
  }
  
  void testOnlyLocal(){
    assertEquals(pn3.localName , 'onlyLocal')
  }
}
