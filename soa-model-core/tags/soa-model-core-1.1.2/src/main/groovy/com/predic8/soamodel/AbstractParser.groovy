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

import javax.xml.stream.*
import com.predic8.xml.util.*
import com.predic8.util.*
import com.predic8.io.*
import com.predic8.schema.*
import org.apache.commons.logging.*

abstract class AbstractParser{
  
  private Log log = LogFactory.getLog(this.class)
  
  def resourceResolver = new ExternalResolver()
  
  def parse(params){
    updateParams(params)
    log.debug("AbstractParser: params.newBaseDir: ${params.newBaseDir} , params.input: " + params.input)
    log.debug("parsing " + params.input + " input from baseDir: ${params.baseDir}")
    parseLocal(getResourceToken(params), params)
  }

  private updateParams(params) {
    params.baseDir = params.baseDir ?: ''
    params.newBaseDir = HTTPUtil.updateBaseDir(params.input, params.baseDir)
    params.resourceResolver = params.resourceResolver ?: resourceResolver
    params.wsiResults = params.wsiResults ?: []
  }

  private getResourceToken(params) {
      getToken(resourceResolver.resolve(params.input, params.baseDir))
  }

  private getToken(res) {
      XMLInputFactory.newInstance().createXMLStreamReader(res)
  }
}
