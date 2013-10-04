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

package com.predic8.policy

import javax.xml.namespace.QName;

import com.predic8.soamodel.Consts;

class PolicyItemUtility {
	
	public static getPolicyItem(QName qname){
		switch (qname.localPart) {
				case 'Policy' : return new Policy(ELEMENTNAME: qname) ; break 
				case 'ExactlyOne' : return new ExactlyOne(ELEMENTNAME: qname) ; break 
				case 'All' : return new All(ELEMENTNAME: qname) ; break
				case 'UsingAddressing' : return new UsingAddressing(ELEMENTNAME: qname) ; break
				case 'SymmetricBinding' : return new SymmetricBinding(ELEMENTNAME: qname) ; break
				case 'Header' : return new Header(ELEMENTNAME: qname) ; break
				case 'Body' : return new Body(ELEMENTNAME: qname) ; break
				case 'AlgorithmSuite' : return new AlgorithmSuite(ELEMENTNAME: qname) ; break
				case 'UsernameToken' : return new UsernameToken(ELEMENTNAME: qname) ; break
				case 'ProtectionToken' : return new ProtectionToken(ELEMENTNAME: qname) ; break
				case 'X509Token' : return new X509Token(ELEMENTNAME: qname) ; break
				case 'WssX509V3Token10' : return new WssX509V3Token10(ELEMENTNAME: qname) ; break
				case 'Layout' : return new Layout(ELEMENTNAME: qname) ; break
				case 'Strict' : return new Strict(ELEMENTNAME: qname) ; break
				case 'IncludeTimestamp' : return new IncludeTimestamp(ELEMENTNAME: qname) ; break
				case 'OnlySignEntireHeadersAndBody' : return new OnlySignEntireHeadersAndBody(ELEMENTNAME: qname) ; break
				case 'Basic128' : return new Basic128(ELEMENTNAME: qname) ; break
				case 'Wss11' : return new Wss11(ELEMENTNAME: qname) ; break
				case 'MustSupportRefKeyIdentifier' : return new MustSupportRefKeyIdentifier(ELEMENTNAME: qname) ; break
				case 'MustSupportRefIssuerSerial' : return new MustSupportRefIssuerSerial(ELEMENTNAME: qname) ; break
				case 'MustSupportRefThumbprint' : return new MustSupportRefThumbprint(ELEMENTNAME: qname) ; break
				case 'MustSupportRefEncryptedKey' : return new MustSupportRefEncryptedKey(ELEMENTNAME: qname) ; break
				case 'SignedSupportingTokens' : return new SignedSupportingTokens(ELEMENTNAME: qname) ; break
				case 'WssUsernameToken10' : return new WssUsernameToken10(ELEMENTNAME: qname) ; break
				case 'EncryptedParts' : return new EncryptedParts(ELEMENTNAME: qname) ; break
				case 'SignedParts' : return new SignedParts(ELEMENTNAME: qname) ; break
				case 'TransportBinding' : return new TransportBinding(ELEMENTNAME: qname) ; break
				case 'TransportToken' : return new TransportToken(ELEMENTNAME: qname) ; break
		 }
	}
}

