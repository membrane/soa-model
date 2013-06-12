package com.predic8.wsdl

import com.predic8.xml.util.ClasspathResolver

class StyleTest extends GroovyTestCase {
	
	Definitions wsdl1
	Definitions wsdl2
	Definitions wsdl3
	Definitions wsdl4
	Definitions wsdl5
	
	void setUp() {
		
    def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
    wsdl1 = parser.parse(new WSDLParserContext(input:"/hotel.wsdl"))
    wsdl2 = parser.parse(new WSDLParserContext(input:"/RPCStyle.wsdl"))
    wsdl3 = parser.parse(new WSDLParserContext(input:"/BLZService.wsdl"))
    wsdl4 = parser.parse(new WSDLParserContext(input:"/diff/part-with-type/original.wsdl"))
    wsdl5 = parser.parse(new WSDLParserContext(input:"/diff/part-with-type/modified.wsdl"))
	}
	
	void testDefinitionsStyle() {
		assertEquals('Document/Literal', wsdl1.getStyle())
		assertEquals('Rpc/Encoded', wsdl2.getStyle())
		assertEquals('Document/Literal-Wrapped', wsdl3.getStyle())
		assertEquals('Document/Literal-Wrapped', wsdl4.getStyle())
		assertEquals('Document/Literal (Invalid! The input message of operation PessoaFisicaNFe uses a part with type instead of element.)', wsdl5.getStyle())
	}
}
