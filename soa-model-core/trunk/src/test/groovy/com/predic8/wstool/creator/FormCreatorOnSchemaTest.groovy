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

package com.predic8.wstool.creator

import com.predic8.schema.SchemaParser;
import junit.framework.TestCase
import groovy.xml.*
import com.predic8.xml.util.*


class FormCreatorOnSchemaTest extends GroovyTestCase{
  
  def schema
  
  public void setUp(){
    def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
    schema = parser.parse(input:"/formCreator/schema1.xsd")
  }
  
//@TODO: write a better test that make sense!  
  void testFormCreater() {
    def strWriter = new StringWriter()
    def creator = new FormCreator(builder : new MarkupBuilder(strWriter))
    schema.getElement('test').create(creator, new FormCreatorContext(formParams:'',path:"xpath:/"))
    /*new File('C:/temp/FormCreatorTest/schemaFormCreator.html').write("""
    <html>
      <head>
        <link rel='stylesheet' href='file://C:/temp/FormCreatorTest/css/main.css' />
        <link rel='stylesheet' href='file://C:/temp/FormCreatorTest/css/registry.css' />
        <link rel='stylesheet' href='file://C:/temp/FormCreatorTest/css/tab.css' />
        <script type="text/javascript" src="file://C:/temp/FormCreatorTest/x/lib/xgetelementbyid.js" ></script> 
        <script type="text/javascript" src="file://C:/temp/FormCreatorTest/x/lib/xdef.js" ></script>   
        <script type="text/javascript" src="file://C:/temp/FormCreatorTest/x/lib/xvisibility.js" ></script>  
        <script type='text/javascript' src='file://C:/temp/FormCreatorTest/js/service-repository.js'></script>
      </head>
      <body>$strWriter</body>
    </html>""")*/
  }
}
