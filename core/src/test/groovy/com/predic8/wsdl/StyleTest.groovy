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
    wsdl1 = parser.parse("/hotel.wsdl")
    wsdl2 = parser.parse("/RPCStyle.wsdl")
    wsdl3 = parser.parse("/BLZService.wsdl")
    wsdl4 = parser.parse("/diff/part-with-type/original.wsdl")
    wsdl5 = parser.parse("/diff/part-with-type/modified.wsdl")
	}
	
	void testDefinitionsStyle() {
		assert 'Document/Literal' == wsdl1.getBinding('hotelBinding').getStyle()
		assert 'Rpc/Encoded' == wsdl2.getBinding('getExchangeRateBinding').getStyle()
		assert 'Document/Literal-Wrapped' == wsdl3.getBinding('BLZServiceSOAP11Binding').getStyle()
		assert 'Document/Literal-Wrapped' == wsdl3.getBinding('BLZServiceSOAP12Binding').getStyle()
		assert 'This binding uses the HTTP protocol and has no style information' == wsdl3.getBinding('BLZServiceHttpBinding').getStyle()
		assert 'Document/Literal-Wrapped' == wsdl4.getBinding('CDCSoap').getStyle()
		assert 'Document/Literal-Wrapped' == wsdl4.getBinding('CDCSoap12').getStyle()
		assert 'This binding uses the HTTP protocol and has no style information' == wsdl4.getBinding('CDCHttpGet').getStyle()
		assert 'This binding uses the HTTP protocol and has no style information' == wsdl4.getBinding('CDCHttpPost').getStyle()
		assert 'Document/Literal' == wsdl5.getBinding('CDCSoap').getStyle()
		assert 'Document/Literal' == wsdl5.getBinding('CDCSoap12').getStyle()
	}
}
