package com.predic8.xml.util

class UtfFixTest extends GroovyTestCase {
  byte[] combineArrays(foo, bar) {
    def tmp = new byte[foo.length + bar.length]
    System.arraycopy(foo, 0, tmp, 0, foo.length)
    System.arraycopy(bar, 0, tmp, foo.length, bar.length)
    tmp
  }
  
    void testFixUtf() {
        def bom = [0xEF, 0xBB, 0xBF] as byte[]
        def replacementChar = [0xEF, 0xBF, 0xBD] as byte[]

        def document = '<wsdl:definitions'
        def documentWithBom = combineArrays(bom, document.bytes)
        def documentWithReplacementChar = combineArrays(replacementChar, document.bytes)
        def documentWithTwoReplacementChars = combineArrays(replacementChar, documentWithReplacementChar)
        def documentWithThreeReplacementChars = combineArrays(replacementChar, documentWithTwoReplacementChars)

        // creates an instance of an anonymous inner class
        def resolver = new ResourceResolver() {}

        assert resolver.fixUtf(new ByteArrayInputStream(document.bytes)).text == '<wsdl:definitions'
        assert resolver.fixUtf(new ByteArrayInputStream(documentWithBom)).text == '<wsdl:definitions'
        assert resolver.fixUtf(new ByteArrayInputStream(documentWithReplacementChar)).text == '<wsdl:definitions'
        assert resolver.fixUtf(new ByteArrayInputStream(documentWithTwoReplacementChars)).text == '<wsdl:definitions'
        // only replaces the first two replacement characters
        assert resolver.fixUtf(new ByteArrayInputStream(documentWithThreeReplacementChars)).text == new String(replacementChar) + '<wsdl:definitions'
    }
}
