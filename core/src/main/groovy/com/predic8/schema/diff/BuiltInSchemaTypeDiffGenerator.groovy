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

package com.predic8.schema.diff

import com.predic8.soamodel.*

class BuiltInSchemaTypeDiffGenerator extends AbstractDiffGenerator {
	
	def generator
	def a,b
	
	
	/**
	 * This method was generated for completeness.
	 * The diffs will be detected local by the containing element.
	 * Don't implement this method, or remove the duplicates before
	 * from e.g. WSDLDiffGenerator(l. 242)
	 */
	public List<Difference> compare() {
		[]
	}


	@Override
  protected Object updateLabels() {
	  return null;
  }
}

