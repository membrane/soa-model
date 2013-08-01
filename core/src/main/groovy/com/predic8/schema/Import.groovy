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

package com.predic8.schema

import com.predic8.soamodel.CreatorContext
import com.predic8.soamodel.KnownSchemas

import javax.xml.namespace.QName as JQName

import org.apache.commons.logging.*

import com.predic8.xml.util.*
//import com.predic8.soamodel.KnownSchemas
import static com.predic8.soamodel.Consts.SCHEMA_NS

class Import extends SchemaComponent {

	String namespace
	String schemaLocation
	Schema importSchema

	/**
	 * Maps the imported schema documents with well-known namespaces
	 * to the corresponding xsd file from the class path. 
	 */
	Map knownDocs

	private Log log = LogFactory.getLog(this.class)

	protected parseAttributes(token, ctx){
		namespace = token.getAttributeValue( null , 'namespace')
		schemaLocation = token.getAttributeValue( null , 'schemaLocation')
		log.debug("import: $schemaLocation , ns: $namespace , schema.basedir: ${schema.baseDir}")
		if(!schemaLocation){
			knownDocs = schema.resourceResolver.knownDocs
			return
		}
		importSchema = ctx.importedSchemas[namespace] ?: parseImportedSchema(input:this, importedSchemas:ctx.importedSchemas)
	}

	def getImportSchema() {
		if(importSchema) return importSchema

		if(schema.definitions?.localSchemas.find{it.targetNamespace==namespace}) {
			log.debug("Inlined schema [$namespace] import resolving from the WSDL.")
			return importSchema = schema.definitions?.localSchemas.find{it.targetNamespace==namespace}
		}

		if(namespace in knownDocs.keySet()){
			log.debug("Well-known schema [$namespace] import resolving from the class path.")
			//TODO It should be possible to use other resourceResolvers expect than ClasspathResolver.
			return importSchema = (new SchemaParser(resourceResolver: new ClasspathResolver())).parse(knownDocs[namespace])
		}
	}

	def create(creator, CreatorContext ctx){
		creator.createImport(this, ctx.clone())
	}

	protected getElementName(){
		new JQName(SCHEMA_NS, 'import')
	}

	String toString(){
		"import[ namespace=$namespace, schemaLocation=$schemaLocation ]"
	}

	private parseImportedSchema(ctx) {
		ctx.baseDir = schema.baseDir
		def impSchema = (new SchemaParser(resourceResolver: schema.resourceResolver)).parse(ctx)
		log.debug("importedSchem.baseDir: ${impSchema.baseDir} , namespace: ${namespace}")
		impSchema
	}
}
