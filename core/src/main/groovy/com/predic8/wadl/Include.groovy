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

package com.predic8.wadl

import javax.xml.namespace.QName

import com.predic8.schema.Schema
import com.predic8.schema.SchemaParser
import com.predic8.schema.SchemaParserContext
import com.predic8.soamodel.Consts

class Include extends WADLElement {

	public static final QName ELEMENTNAME = new QName(Consts.WADL_NS, 'include')

	String href
	Schema schema
	def resourceResolver
	def baseDir
	
	
	protected parseAttributes(token, ctx){
		href = token.getAttributeValue( null , 'href')
		baseDir = ctx.newBaseDir
		resourceResolver = ctx.resourceResolver
	}
	
	Schema getSchema() {
		if(schema) return schema
		schema = parseIncludedSchema()
	}
	
	private Schema parseIncludedSchema(){
		try {
			new SchemaParser(resourceResolver: resourceResolver).parse(new SchemaParserContext(baseDir : baseDir, input: href))
		} catch (Exception e) {
			e.printStackTrace()
		}
	}
	
	String toString() {
		"include[docs: $docs, href: $href]"
	}
}