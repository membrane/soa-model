package com.predic8.schema;

import com.predic8.xml.util.ClasspathResolver;

public class SchemaVersionTest extends GroovyTestCase {
    def schema

    void setUp() {
        def parser = new SchemaParser(resourceResolver: new ClasspathResolver())
        schema = parser.parse("/schema/version/schema-version.xsd")
    }

    void testSchemaVersion() {
        def expected = "1.0"
        def version = schema.version
        assertEquals( expected, version )
    }
}
