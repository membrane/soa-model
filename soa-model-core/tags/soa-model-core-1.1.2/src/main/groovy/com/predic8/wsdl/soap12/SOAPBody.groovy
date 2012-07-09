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

package com.predic8.wsdl.soap12;

import javax.xml.namespace.QName as JQName
import com.predic8.soamodel.Consts
import com.predic8.wsdl.*

class SOAPBody extends AbstractSOAPBody{

  public static final JQName ELEMENTNAME = new JQName(Consts.WSDL_SOAP12_NS, 'body')
}
