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


import static com.predic8.soamodel.Consts.SCHEMA_NS

import javax.xml.namespace.QName as JQName

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.predic8.soamodel.CreatorContext
import com.predic8.soamodel.KnownSchemas
import com.predic8.xml.util.*

/**
 * 	SOAP Encoding schema with the namespace 'http://www.w3.org/2003/05/soap-encoding' will 
 * 	not be parsed from the schemaLocation. Instead the copy from Classpath will be used.
 */
class Import extends SchemaComponent {

	String namespace
	String schemaLocation
	Schema importSchema

	private static final Logger log = LoggerFactory.getLogger(Import.class)

	protected parseAttributes(token, ctx){
		namespace = token.getAttributeValue( null , 'namespace')
		schemaLocation = token.getAttributeValue( null , 'schemaLocation')
		log.debug("import: $schemaLocation , ns: $namespace , schema.basedir: ${schema.baseDir}")
		
		// Known schemas will not be parsed from the schemaLocation. Instead the copy from Classpath will be used.
		if(namespace in KnownSchemas.docs.keySet()){
			log.info("Loading schema '$namespace' provided by SOA Model instead of user-provided document.")
			return importSchema = (new SchemaParser(resourceResolver: new ClasspathResolver())).parse(KnownSchemas.docs[namespace])
		}
		
		if(!schemaLocation)	return
		
		importSchema = ctx.getImportedSchema(this)
	}

	def getImportSchema() {
		if(importSchema) return importSchema

		if(schema.definitions?.localSchemas.find{it.targetNamespace == namespace}) {
			log.debug("Inlined schema [$namespace] import resolving from the WSDL.")
			return importSchema = schema.definitions?.localSchemas.find{it.targetNamespace==namespace}
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

	protected parseImportedSchema(ctx) {
		ctx.baseDir = schema.baseDir
		def impSchema = (new SchemaParser(resourceResolver: schema.resourceResolver)).parse(ctx)
		log.debug("importedSchem.baseDir: ${impSchema.baseDir} , namespace: ${namespace}")
		impSchema
	}
}
