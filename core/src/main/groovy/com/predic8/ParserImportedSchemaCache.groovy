package com.predic8

import com.predic8.schema.Schema
import groovy.transform.Synchronized

/**
 * Thread safe imported Schema cache to be used in the {@link com.predic8.soamodel.AbstractParserContext}s
 */
class ParserImportedSchemaCache {

  @SuppressWarnings("GroovyUnusedDeclaration")
  Object importedSchemasLock = new Object()


  final Map<String, Schema> importedSchemas = [:]

  @Synchronized('importedSchemasLock')
  Schema addSchema (Schema schema, String schemaKey = null) {
    String key = schemaKey ?: schema?.targetNamespace

    // Do not try to cache schema's without a meaningful key
    if (!key) { return schema }

    if (!importedSchemas[key]) {
        importedSchemas[key] = schema
    }
    importedSchemas[key]
  }

  @Synchronized('importedSchemasLock')
  Schema addSchema (Closure lazySchema, String schemaKey = null) {
    String key = schemaKey
    if (!schemaKey) {
      // Schema needs to be parsed to determine the cache key
      return addSchema((Schema)lazySchema.call())
    }

    if (!importedSchemas[key]) {
        importedSchemas[key] = (Schema) lazySchema.call()
    }
    importedSchemas[key]
  }

  @Synchronized('importedSchemasLock')
  synchronized Schema getSchema (String key) {
    importedSchemas[key]
  }

}
