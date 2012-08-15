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

   
// Temporary test. Used for bug fixes. Can be changed or deleted later. 
package com.predic8.wstool.creator

import com.predic8.schema.*
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser
import com.predic8.xml.util.*
import groovy.xml.*

class TemporaryRequestCreatorTestTest extends GroovyTestCase{

  Definitions wsdl 

  public void setUp(){
    def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
    wsdl = parser.parse(input:"article/article.wsdl")
  }
  
  void testCreatRequest() {
    def strWriter = new StringWriter()
    HashMap <String, String> formParams = new HashMap <String,String>()
    
    formParams['xpath:/create/article[1]/name[1]']='foo1'
    formParams['xpath:/create/article[1]/description']='bar1'
    formParams['xpath:/create/article[1]/price[1]/amount']='10.00'
    formParams['xpath:/create/article[1]/price[1]/currency']='USD'
    formParams['xpath:/create/article[1]/id']='1'
    
    SOARequestCreator creator = new SOARequestCreator(wsdl, new RequestCreator(), new MarkupBuilder(strWriter))
    creator.formParams = formParams
    
    creator.createRequest('ArticleServicePT', 'create', 'ArticleServicePTBinding')
//    println strWriter
  }
}
