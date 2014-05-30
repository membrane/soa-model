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

package com.predic8.soamodel

import javax.xml.namespace.QName


class Consts {

	//WSDL, Schema and SOAP namespaces
  public static final String WSDL11_NS = "http://schemas.xmlsoap.org/wsdl/"
	public static final String WSDL20_NS = "http://www.w3.org/ns/wsdl"
  public static final String WSDL_SOAP11_NS = "http://schemas.xmlsoap.org/wsdl/soap/"
  public static final String WSDL_SOAP12_NS = "http://schemas.xmlsoap.org/wsdl/soap12/"
  public static final String WSDL_HTTP_NS = "http://schemas.xmlsoap.org/wsdl/http/"
  public static final String SOAP_ENC_NS = "http://schemas.xmlsoap.org/soap/http"
  public static final String SCHEMA_NS = "http://www.w3.org/2001/XMLSchema"
  public static final String XML_NS = "http://www.w3.org/XML/1998/namespace"
  public static final String SOAP11_NS = "http://schemas.xmlsoap.org/soap/envelope/"
  public static final String SOAP12_NS = "http://www.w3.org/2003/05/soap-envelope"
	
	//WS-Policy 1.5 namespaces
  public static final String SP15_NS = "http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200702"
  public static final String WSAM_NS = "http://www.w3.org/2007/05/addressing/metadata"
  public static final String WSP15_NS = "http://www.w3.org/ns/ws-policy"

	//WS-Policy 1.2 namespaces
  public static final String WSP12_NS = "http://schemas.xmlsoap.org/ws/2004/09/policy"
  public static final String SP12_NS = "http://schemas.xmlsoap.org/ws/2005/07/securitypolicy"
  public static final String WSSE_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
  public static final String WSU_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
	
  public static final String WSAWS05_NS ="http://www.w3.org/2005/08/addressing"
  public static final String WSAWS06_NS = "http://www.w3.org/2006/05/addressing/wsdl"

  public static final String WSRM_NS = "http://schemas.xmlsoap.org/ws/2005/02/rm/policy"

	//WADL namespaces
	public static final String WADL_NS = "http://wadl.dev.java.net/2009/02"

	public static final List<String> POLICY_NAMESPACES = [SP15_NS, WSAM_NS, WSP15_NS, WSP12_NS, SP12_NS, WSSE_NS, WSU_NS, WSAWS05_NS, WSAWS06_NS, WSRM_NS]
	
}
