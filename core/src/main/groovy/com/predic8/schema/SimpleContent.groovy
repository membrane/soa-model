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

package com.predic8.schema

import java.util.List

import javax.xml.namespace.QName as JQName

import com.predic8.soamodel.AbstractDiffGenerator;
import com.predic8.soamodel.CreatorContext
import com.predic8.soamodel.DiffGeneratorContext;
import com.predic8.schema.restriction.*
import com.predic8.schema.restriction.Restriction as SimpleTypeRestriction

import static com.predic8.soamodel.Consts.SCHEMA_NS

class SimpleContent extends SchemaComponent {

  //Derivation can not be used as a superclass. Because restriction is a simpleRestriction in this case.
  Extension extension
  BaseRestriction restriction

  protected parseChildren(token, child, params) {
    switch (child ){
      case 'extension' :
        extension = new Extension(schema: schema)
          extension.parse(token, params) ; break
      case 'restriction' :
        def base = getTypeQName(token.getAttributeValue( null , 'base'))
				if(base) {
					def type = base.localPart
					if(base.namespaceURI == SCHEMA_NS){
						restriction = RestrictionUtil.getRestriction(type, [base: base])
						restriction.schema = schema
					} else {
						restriction = new BaseRestriction(base : base, schema: schema)
					}
				} else {
					restriction = new SimpleTypeRestriction(schema: schema)
				}
        restriction.parse(token, params) ; break
    }
  }

  def getDerivation() {
    return extension ?: restriction
  }

  protected getElementName(){
    new JQName(SCHEMA_NS, 'simpleContent')
  }

  def create(creator, CreatorContext ctx){
    creator.createSimpleContent(this, ctx)
  }

  def compare(AbstractDiffGenerator generator, other, DiffGeneratorContext ctx = new DiffGeneratorContext()){
    generator.compareSimpleContent(this, other, ctx)
  }
}

