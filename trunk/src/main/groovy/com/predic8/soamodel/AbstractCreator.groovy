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

abstract class AbstractCreator {

  def builder


  protected getDisplayName(String name, target, error){
    if(error && error.expr.target?.target == target && error.expr.errorValues.contains(name)) {
      return "[mark]${name}[/mark]"
    }
    return name
  }
  
  protected Map<String, String> getNamespaceAttributes(XMLElement element){
    def attrs = [:]
    (element.namespaces - element.parent?.namespaceContext).each {
      if(it.key) attrs.put('xmlns:'+it.key, it.value)
      else attrs.put('xmlns' , it.value)
    }
    attrs
  }
}

