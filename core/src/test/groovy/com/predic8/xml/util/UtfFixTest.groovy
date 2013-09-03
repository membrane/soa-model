package com.predic8.xml.util

class UtfFixTest extends GroovyTestCase {
    void testFixUtf() {
        def bom = [0xEF, 0xBB, 0xBF] as byte[]
        def replacementChar = [0xEF, 0xBF, 0xBD] as byte[]

        def document = '<wsdl:definitions'
        def documentWithBom = new String(bom) + document
        def documentWithReplacementChar = new String(replacementChar) + document
        def documentWithTwoReplacementChars = new String(replacementChar) * 2 + document
        def documentWithThreeReplacementChars = new String(replacementChar) * 3 + document

        // creates an instance of an anonymous inner class
        def resolver = new ResourceResolver() {}

        assert resolver.fixUtf(new ByteArrayInputStream(document.bytes)).text == '<wsdl:definitions'
        assert resolver.fixUtf(new ByteArrayInputStream(documentWithBom.bytes)).text == '<wsdl:definitions'
        assert resolver.fixUtf(new ByteArrayInputStream(documentWithReplacementChar.bytes)).text == '<wsdl:definitions'
        assert resolver.fixUtf(new ByteArrayInputStream(documentWithTwoReplacementChars.bytes)).text == '<wsdl:definitions'
        // only replaces the first two replacement characters
        assert resolver.fixUtf(new ByteArrayInputStream(documentWithThreeReplacementChars.bytes)).text == new String(replacementChar) + '<wsdl:definitions'
    }
}
