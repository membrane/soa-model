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

class Difference {
  
  String description
  List<Difference> diffs = []
  String type
  boolean safe
  boolean breaks
  def a
  def b
  
  def dump(level = 0) {
    def str = description + '\n'
    level++
    diffs.each{
      str += ' '*level+it.dump(level)
    }
    str
  }
  
  public String toString(){    
    "${description}\n${diffs.join('\n')}"
  }
  
  def breaks(){
    if(breaks) return true
    def res = false
    diffs.each{
      if(it.breaks()) res = true
    }
    return res
  }
  
  def safe(){
    if(!safe) return false
    def res = true
    diffs.each{
      if(! it.safe()) res = false
    }
    return res
  }
  
}