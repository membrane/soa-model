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

import org.apache.commons.httpclient.methods.*
import org.apache.commons.httpclient.*

import com.predic8.schema.*
import com.predic8.io.*
import com.predic8.util.*

class ClasspathResolver extends ResourceResolver {
  

  def resolve(input, baseDir) {
    if ( input instanceof Import ) {
        input = input.schemaLocation
    }
    if ( input instanceof Reader ) {
      return input
    }
    
    def resource = this.class.getResourceAsStream(getLocation(input, baseDir))
    if (!resource) throw new RuntimeException("Could not get resource for ${getLocation(input, baseDir)}")
    resource
  }
  
  def private getLocation(input, baseDir) {
    if(input.startsWith('/') || input.startsWith('\\')){
      return input
    }
    HTTPUtil.normalize("/$baseDir/$input")
  }
  
}
