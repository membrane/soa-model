package com.predic8.policy.creator

import groovy.xml.*

import com.predic8.wsdl.*
import com.predic8.wsdl.creator.*
import com.predic8.xml.util.ClasspathResolver

class PolicyCreatorTest extends GroovyTestCase {
	
	Definitions wsdl
	
	void setUp() {
		def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
		wsdl = parser.parse('policy/PolicyArticle.wsdl')
	}
	
	void testPolicyCreatorFromWSDL() {
		def strWriter = new StringWriter()
		def creator = new WSDLCreator(builder : new MarkupBuilder(strWriter))
		creator.createDefinitions(wsdl, new WSDLCreatorContext())
		def parser = new WSDLParser(resourceResolver: new ClasspathResolver())
		def generatedWSDL = parser.parse(new ByteArrayInputStream(strWriter.toString().bytes))
		assert wsdl.policies.values()*.allPolicyItems.size() == generatedWSDL.policies.values()*.allPolicyItems.size() // 3 items
		assert wsdl.policies.values()*.allPolicyItems.flatten().size() == generatedWSDL.policies.values()*.allPolicyItems.flatten().size() // 61 items 
		
		assert '#ArticleBinding_GetArticle_Input_Policy' == generatedWSDL.bindings.operations.input.flatten()[0].policyReference.uri
		assert '#ArticleBinding_GetArticle_Output_Policy' == generatedWSDL.bindings.operations.output.flatten()[0].policyReference.uri
	}
	
}
