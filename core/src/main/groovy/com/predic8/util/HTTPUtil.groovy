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

package com.predic8.util

import com.predic8.schema.Import as SchemaImport
import com.predic8.wsdl.Import as WsdlImport

class HTTPUtil {
  
  public static String updateBaseDir(input,oldBaseDir) {
    if(input instanceof SchemaImport) input = input.schemaLocation
		if(input instanceof WsdlImport) input = input.location
    if(! (input instanceof String)) return oldBaseDir
       
    def comps = input.split(/[\\\/]/)
    
		//Find out if input is an absolute adress. If so, the oldBaseDir is not needed.
    if(input.startsWith('/') || input.startsWith('\\') || input.startsWith('http') || input.matches(/^([A-Z]|[a-z]):\/.*$/)){
      return comps[0..-2].join('/')+'/'
    }
    if(comps.size()==1) return oldBaseDir
    if(oldBaseDir) oldBaseDir += '/'
    def path = oldBaseDir + comps[0..-2].join('/')+'/'
    normalize(path)
  }
  
  public static String getLocation(baseDir, location) {
    if(location.startsWith('http') || location.startsWith('https') || location.startsWith('/') || location.startsWith('\\') )
      return location
    baseDir+location
  }
  
  protected static normalize(path){
    path.replaceAll('\\\\', '/').replaceAll('/+',"/").replaceAll(':/','://')    // beim Verwenden von /pattern/ gab es Compilerfehler
  }
  
}