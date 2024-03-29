/* Copyright 2012 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.schema;

import groovy.xml.*
import groovy.namespace.*

import com.predic8.schema.creator.*
import com.predic8.wstool.creator.*
import com.predic8.soamodel.*

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class SchemaComponent extends XMLElement{
  
	private static final Logger log = LoggerFactory.getLogger(SchemaComponent.class)
	
	static final String NAMESPACE = Consts.SCHEMA_NS
	
  Schema schema
  String name
  Annotation annotation
	
	def exchange = [] as Set //For WSDL message direction.

  protected parseAttributes(token, params){
    name = token.getAttributeValue( null , 'name')
  }

  protected parseChildren(token, child, params){
    switch (child ){
      case 'annotation' :
	      annotation = new Annotation(schema: schema)
	      annotation.parse(token, params) ; break
    }
  }
	
	String getNamespaceUri() {
		schema.targetNamespace
	}
	
/**
 * Is used by RequestCreator and RequestTemplateCreator
 * to get the prefix of the targetNamespace of the created element, like p8, ns0, ns1.	
 * @return Prefix of the schema targetNamespace. 
 */
	String getPrefix(){
		getPrefix(schema.targetNamespace)
	}

  String getSchemaFragment(ctx){
    if ( !ctx.declNS ) {
      ctx.declNS=[:]
    }
    def writer = new StringWriter()
    create(new SchemaCreator(builder:new MarkupBuilder(writer)), ctx)
    writer.toString()
  }

  def create(creator,CreatorContext ctx){
    throw new RuntimeException("missing method create(creator,CreatorContext ctx) for class ${getElementName()} !")
  }
  
  String getAsString(){
    StringWriter writer = new StringWriter();
    create(new SchemaCreator(builder:new MarkupBuilder(writer)), new SchemaCreatorContext());
    writer.toString()
  }
  
  String getRequestTemplate(){
    def writer = new StringWriter()
    create(new RequestTemplateCreator(builder:new MarkupBuilder(writer)),new RequestTemplateCreatorContext())
    writer.toString()
  }
  
}
