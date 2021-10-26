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

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.predic8.io.*
import com.predic8.schema.*
import com.predic8.util.*
import com.predic8.xml.util.*

abstract class AbstractParser{

	private static final Logger log = LoggerFactory.getLogger(AbstractParser.class)

	def resourceResolver = new ExternalResolver()

	
		protected parse(AbstractParserContext ctx) {
		updatectx(ctx)
		log.debug("AbstractParser: ctx.newBaseDir: ${ctx.newBaseDir} , ctx.input: " + ctx.input)
		log.debug("parsing " + ctx.input + " input from baseDir: ${ctx.baseDir}")
		parseLocal(getResourceToken(ctx), ctx)
	}

	private updatectx(ctx) {
		ctx.baseDir = ctx.baseDir ?: ''
		ctx.newBaseDir = HTTPUtil.updateBaseDir(ctx.input, ctx.baseDir)
		ctx.resourceResolver = ctx.resourceResolver ?: resourceResolver
		ctx.wsiResults = ctx.wsiResults ?: []
		ctx.errors = ctx.errors ?: []
	}

	private getResourceToken(ctx) {
		getToken(resourceResolver.resolve(ctx.input, ctx.baseDir))
	}

	private getToken(res) {
		def inputFactory = XMLInputFactory.newInstance()
		inputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false)
		inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false)
		inputFactory.createXMLStreamReader(res)
	}
}
