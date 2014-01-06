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

import com.predic8.ParserImportedSchemaCache
import com.predic8.schema.Import
import com.predic8.schema.Schema
import com.predic8.xml.util.ResourceResolver

abstract class AbstractParserContext {

  def input
  ResourceResolver resourceResolver
  String baseDir
  String newBaseDir
  def parent
  String targetNamespace
  def token
  def wsiResults = []
	def errors = []
	def validated = []

  //TODO ITHENA what with Included schema's? (com.predic8.schema.Include & com.predic8.wadl.Include)
  ParserImportedSchemaCache importedSchemaCache = new ParserImportedSchemaCache()

  abstract createNewSubContext(args)

  /**
   * Gets the imported statement.
   * <p/>
   * Reuses an already parsed schema if available, parses and caches the schema if not cached yet.
   *
   * @param importStatement
   * @return the schema
   */
  Schema getImportedSchema(Import importStatement) {
    importedSchemaCache.addSchema(
        { importStatement.parseImportedSchema(createNewSubContext([input: importStatement])) },
        getSchemaCacheKey(importStatement))
  }

  Schema setImportedSchema(Schema schema) {
    importedSchemaCache.addSchema(schema, getSchemaCacheKey(schema))
  }

  String getSchemaCacheKey(Import importStatement) {
    "${importStatement?.namespace}${extractFileName(importStatement?.schemaLocation)}"
  }

  String getSchemaCacheKey(Schema schema) {
      "${schema?.targetNamespace}${extractFileName(schema?.schemaLocation)}"
  }

  /**
   * The caching key must no use the entire schemaLocation since it might be relative
   * from the importing WSDL or Schema.
   */
  protected String extractFileName(String schemaLocation) {
      if (!schemaLocation) { return '' }
      if (schemaLocation.size() == 1) { return schemaLocation }

      int separatorIndex = schemaLocation.lastIndexOf(File.separator)
      schemaLocation[separatorIndex+1..-1]
  }

}

