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

package com.predic8.soamodel

import com.predic8.util.*

class UpdateBaseDirTest extends GroovyTestCase{
  void testUpdateBaseDir(){
    assertEquals("data/", HTTPUtil.updateBaseDir("data\\Planning.xsd", ''))
    assertEquals("data/", HTTPUtil.updateBaseDir("data/Planning.xsd", ''))
    assertEquals("a/b/", HTTPUtil.updateBaseDir("b/foo.xsd", 'a'))
    assertEquals("data", HTTPUtil.updateBaseDir("Planning.xsd", 'data'))
    assertEquals("", HTTPUtil.updateBaseDir("Planning.xsd", ''))
  }
  
  void testNormalizer(){
    assertEquals('/' , HTTPUtil.normalize('///'))
    assertEquals('/a/b/' , HTTPUtil.normalize('\\\\a\\\\b/'))
  }
  
  void testHttpUrls() {
    assertEquals("http://x.com/data/", HTTPUtil.updateBaseDir("http://x.com/data/Planning.xsd", ''))
    assertEquals("http://x.com/data/soup/", HTTPUtil.updateBaseDir("http://x.com/data/soup/Planning.xsd", ''))
    assertEquals("http://x.com/data/soup/", HTTPUtil.updateBaseDir("blub.xsd", 'http://x.com/data/soup/'))
  }
}