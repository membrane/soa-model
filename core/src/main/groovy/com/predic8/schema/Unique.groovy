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

import static com.predic8.soamodel.Consts.SCHEMA_NS
import groovy.xml.*

import com.predic8.soamodel.CreatorContext
import com.predic8.wstool.creator.*

class Unique extends SchemaComponent {

	Selector selector
	List<Field> fields = []
	String id
	
	protected parseAttributes(token, params){
		super.parseAttributes(token, params)
		id = token.getAttributeValue( null , 'id')
	}
	
	protected parseChildren(token, child, params){
		super.parseChildren(token, child, params)
		switch (child ){
			case 'selector' :
				selector  = new Selector(schema: schema)
				selector.parse(token, params) ; break
			case 'field' :
				def field = new Field(schema: schema)
				field.parse(token, params) 
				fields << field ; break
		}
	}

  protected getElementName(){
    'unique'
  }
	
	def create(creator, CreatorContext ctx){
		creator.createUnique(this, ctx.clone())
	}

}
