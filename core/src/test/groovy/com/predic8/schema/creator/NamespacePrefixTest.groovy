package com.predic8.schema.creator

import com.predic8.soamodel.CreatorContext

class NamespacePrefixTest extends GroovyTestCase {
	
	CreatorContext ctx = new CreatorContext()
	def creator = new SchemaCreator()
	
	void setUp() {
		ctx.declNS = ['FirstNamespace':['ns1'] , 'SecondNamespace':['ns2']]
	}
	
	void testNewPrefix(){
		assertEquals('ns3', creator.getUnusedPrefix('ns1', ctx))
	}

}
