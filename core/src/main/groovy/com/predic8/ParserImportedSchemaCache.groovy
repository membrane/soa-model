package com.predic8

import com.predic8.schema.Schema

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * Thread safe imported Schema cache to be used in the {@link com.predic8.soamodel.AbstractParserContext}s
 */
class ParserImportedSchemaCache {

  final ConcurrentMap<String, Schema> importedSchemas = new ConcurrentHashMap()

  Schema addSchema (Schema schema, String schemaKey = null) {
    String key = schemaKey ?: schema?.targetNamespace
    importedSchemas.putIfAbsent(key, schema)
  }

  Schema addSchema (Closure lazySchema, String schemaKey = null) {
    String key = schemaKey
    if (!schemaKey) {
      // Schema needs to be parsed to determine the cache key
      return addSchema((Schema)lazySchema.call())
    }
    importedSchemas[key] ?: importedSchemas.putIfAbsent(key, (Schema)lazySchema.call())
  }

  Schema getSchema (String key) {
    importedSchemas[key]
  }

}
