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

class PrefixedName {
  
  String prefix = ''
  String localName
  
  def PrefixedName(prefix, localName){
    this.prefix = prefix
    this.localName = localName
  }
  
  def PrefixedName(name) {
    def tempName = name.split(':')
    if(tempName.size() == 2) {
      this.prefix = tempName[0]
      this.localName = tempName[1]
    }
    else if(tempName.size() == 1) {
      this.localName = name
    }
  }
  
  String toString(){
    if(prefix) return "$prefix:$localName" 
    localName
  }
}
