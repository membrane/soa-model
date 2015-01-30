package com.predic8.wadl

import com.predic8.soamodel.Consts
import com.predic8.xml.util.ClasspathResolver

class WADLParserTest extends GroovyTestCase {
	
	WADLParser parser = new WADLParser(resourceResolver: new ClasspathResolver())
	Application wadl
	
	void testParser(){
		wadl = parser.parse('wadl/application.xml')
		assert wadl.docs.size() == 2
		assert wadl.docs[1].title == 'A message in the WADL'
		assert wadl.docs[1].lang == 'en'
		assert wadl.docs[1].content == 'This is added to the start of the generated application.wadl'
		assert wadl.grammars.includes.size() == 1
		assert wadl.grammars.includes.parent
		assert wadl.grammars.includes[0].href == 'xsd0.xsd'
		assert wadl.rscss.size() == 1
		//assert 'application' == wadl.rscss[0].parent.elementName.localPart // parent is NULL
		assert wadl.rscss[0].base == "http://example.com:8080/rest"
		assert wadl.rscss[0].resources.size() == 1
		assert wadl.rscss[0].resources[0].path == 'test'
		//assert wadl.rscss[0].resources[0].parent
		assert wadl.rscss[0].resources[0].methods.size() == 2 
		assert wadl.rscss[0].resources[0].methods.name == ['GET', 'POST']
		assert wadl.rscss[0].resources[0].methods.id == ['foo', 'createAuktion']
		assert wadl.rscss[0].resources[0].methods[0].request.params.size() == 2
		assert wadl.rscss[0].resources[0].methods[0].response.representations.size() == 2
		assert wadl.rscss[0].resources[0].methods[1].request.representations[0].mediaType == 'application/xml'
		assert wadl.rscss[0].resources[0].methods[1].request.representations[1].mediaType == 'application/json'
		assert wadl.rscss[0].resources[0].methods[1].request.representations[1].refElementName == 'bar'
		assert wadl.rscss[0].resources[0].methods[1].request.representations[1].element.name == 'bar'
		assert wadl.rscss[0].resources[0].resources[0].path == 'featured'
		//assert 'http://example.com:8080/rest/test/featured' == wadl.rscss[0].resources[0].resources[0].fullPath
		assert wadl.rscss[0].resources[0].resources[1].path == '{id}'
		assert wadl.rscss[0].resources[0].resources[0].methods.size() == 2 
		assert wadl.rscss[0].resources[0].resources[1].params[0].name == 'id' 
		assert wadl.rscss[0].resources[0].resources[1].params[0].style == 'template' 
		assert wadl.rscss[0].resources[0].resources[1].params[0].type.namespaceURI == Consts.SCHEMA_NS
		assert wadl.rscss[0].resources[0].resources[1].params[0].type.localPart == 'int'
	}
}
