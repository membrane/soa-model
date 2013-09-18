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

import com.predic8.schema.restriction.BaseRestriction
import com.predic8.schema.restriction.RestrictionUtil
import com.predic8.schema.restriction.Restriction as SimpleTypeRestriction
import com.predic8.soamodel.AbstractDiffGenerator
import com.predic8.soamodel.CreatorContext
import com.predic8.soamodel.DiffGeneratorContext
import com.predic8.wstool.creator.*

class Selector extends SchemaComponent {
	
	String id
	String xpath

	protected parseAttributes(token, params){
		super.parseAttributes(token, params)
		id = token.getAttributeValue( null , 'id')
		xpath = token.getAttributeValue( null , 'xpath')
	}
	
  protected getElementName() {
    'selector'
  }
	
	def create(creator, CreatorContext ctx) {}

}
