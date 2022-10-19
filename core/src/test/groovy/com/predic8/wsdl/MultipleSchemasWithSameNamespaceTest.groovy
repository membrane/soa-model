package com.predic8.wsdl

import com.predic8.xml.util.ClasspathResolver
import groovy.test.GroovyTestCase

class MultipleSchemasWithSameNamespaceTest extends GroovyTestCase {

    Definitions definitions

    void setUp() {
        definitions = new WSDLParser(resourceResolver: new ClasspathResolver()).parse('/wsdl/multiple-files-same-namespace/service.wsdl')
    }

    void testThatAllSchemaFilesAreLoaded() {
        definitions.getElement('ns1:uploadJson')
        definitions.getElement('ns1:uploadXml')
    }

}
