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
import javax.xml.namespace.QName as JQName
import org.apache.commons.logging.*

import com.predic8.xml.util.*

class Import extends SchemaComponent {
  
  String namespace
  String schemaLocation
  Schema importSchema
  
  private Log log = LogFactory.getLog(this.class)

  protected parseAttributes(token, params){
    namespace = token.getAttributeValue( null , 'namespace')
    schemaLocation = token.getAttributeValue( null , 'schemaLocation')
    log.debug("import: $schemaLocation , ns: $namespace , schema.basedir: ${schema.baseDir}")
    if(!schemaLocation){
      return
    }
    importSchema = params.importedSchemas[namespace] ?: parseImportedSchema(input:this,importedSchemas:params.importedSchemas)
  }
  
  def getImportSchema() {
    if ( !importSchema ) {
      log.debug("inlined schema [$namespace] import resolving.")
      return schema.definitions?.schemas.find{it.targetNamespace==namespace}
    }
    importSchema
  }
  
  def create(creator, CreatorContext ctx){
    creator.createImport(this, ctx.clone())
  }

  protected getElementName(){
    new JQName(Schema.SCHEMA_NS, 'import')
  }
  
  String toString(){
    "import[ namespace=$namespace, schemaLocation=$schemaLocation ]"
  }
  
  private parseImportedSchema(params) {
    params.baseDir = schema.baseDir
    def impSchema = (new SchemaParser(resourceResolver: schema.resourceResolver)).parse(params)
    log.debug("importedSchem.baseDir: ${impSchema.baseDir} , namespace: ${namespace}")
    impSchema
  }
  
}
