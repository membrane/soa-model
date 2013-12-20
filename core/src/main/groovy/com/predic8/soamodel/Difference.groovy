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

import java.util.List;
import java.util.concurrent.Exchanger;

class Difference {
  
  String description
  List<Difference> diffs = []
  String type
	XMLElement original, modified
  private def safe
	//Don't type breaks and safe to boolean. The value may also be null.
  private def breaks
	private def warning
	def exchange = [] as Set //For WSDL message direction.
	
  String dump(level = 0) {
    def str = description +'\n'
    level++
    diffs.each{
      str += ' '*level+it.dump(level)
    }
    str
  }
  
  public String toString(){    
		description
  }
  
  def breaks(){
  	if(breaks) return true
		if(safe) return false
		def res
		if(breaks == false) res = false
		diffs.each{
  		if(it.breaks()) return res = true
  	}
  	res
  }
  
  def safe(){
		if(safe || (breaks() == false)) return true
    if(breaks || warning) return false
    def res
    diffs.each{
    	if(it.breaks()) res = false
      if(it.safe()) res = true
    }
    res
  }
	
	def getWarning() {
		if(warning) return warning
		def res
		diffs.each {
			if(it.getWarning()) return res = true
		}
		res
	}
	
	def exchange(){
		this.exchange.join('')
	}
	
}