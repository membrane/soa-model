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

package com.predic8.wsdl

import java.net.ConnectException;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.junit.internal.runners.statements.Fail;
import com.predic8.xml.util.ExternalResolver;
import com.predic8.xml.util.*
import com.predic8.wsdl.creator.*

class WSDLDependencyFailureTest extends GroovyTestCase{
  
  def resourceResolver 
  
  void setUp() {
   resourceResolver = new ExternalResolver()
  }
  
  void testConnectException(){
    try{
      resourceResolver.resolveAsString("http://localhost")
      assert(false)
    } catch (ResourceDownloadException e) {
      assertEquals(ConnectException, e.rootCause.class)
      assertEquals('http://localhost', e.url)
    } catch (Exception e) {
//      println "excepted [ResourceDownloadException] but was $e" 
      assert(false)
    }
  }
  
  void trestResourceDownloadException(){
    try{
      resourceResolver.resolveAsString("http://www.kaveh-keshavarzi.com/axis2/services2/BLZService?wsdl")
    } catch (ResourceDownloadException e) {
      assertEquals('http://localhost', e.url)
    } catch (Exception e) {
//      println "excepted [ResourceDownloadException] but was $e"
      assert(false)
    }
  }
  
}
