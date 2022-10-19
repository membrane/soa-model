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
package com.predic8.wsdl

import groovy.test.GroovyTestCase

import javax.xml.stream.*

import com.predic8.soamodel.Difference
import com.predic8.wsdl.diff.WsdlDiffGenerator

/**
 * This test is created to deal with the problem described here
 * https://github.com/membrane/soa-model/issues/259
 * 
 * If wsdl is loaded from a file, modified and then exported using 
 * getAsString() it gets generated in default namespace. If the 
 * original wsdl was defined with a namespace prefix and default 
 * namespace was not defined or was defined as something other 
 * than http://schemas.xmlsoap.org/wsdl/, wsdl produced using 
 * getAsString will be invalid due to incorrect namespace.
 * 
 * @author alex.rykov@gmail.com
 *
 */
class NamespaceWsdlTest extends GroovyTestCase{


	def static wsdlWithNsPrefix =
	'''<?xml version="1.0" encoding="UTF-8"?>
<wsdl1:definitions xmlns:wsdl1="http://schemas.xmlsoap.org/wsdl/" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:http="http://schemas.xmlsoap.org/wsdl/http/" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/" xmlns:mime="http://schemas.xmlsoap.org/wsdl/mime/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:tns="http://new.webservice.namespace" targetNamespace="http://new.webservice.namespace">
	<wsdl1:types>
		<xs:schema targetNamespace="http://new.webservice.namespace" elementFormDefault="qualified"/>
	</wsdl1:types>
	<wsdl1:message name="NewMessageRequest">
		<wsdl1:part name="parameter" type="xs:string"/>
	</wsdl1:message>
	<wsdl1:message name="NewMessageResponse">
		<wsdl1:part name="parameter" type="xs:string"/>
	</wsdl1:message>
	<wsdl1:portType name="NewPortType">
		<wsdl1:operation name="NewOperation">
			<wsdl1:input message="tns:NewMessageRequest"/>
			<wsdl1:output message="tns:NewMessageResponse"/>
		</wsdl1:operation>
	</wsdl1:portType>
	<wsdl1:binding name="NewBinding" type="tns:NewPortType">
		<soap:binding style="document" transport="http://schemas.xmlsoap.org/soap/http"/>
		<wsdl1:operation name="NewOperation">
			<soap:operation soapAction="urn:#NewOperation"/>
			<wsdl1:input>
				<soap:body use="literal"/>
			</wsdl1:input>
			<wsdl1:output>
				<soap:body use="literal"/>
			</wsdl1:output>
		</wsdl1:operation>
	</wsdl1:binding>
	<wsdl1:service name="NewService">
		<wsdl1:port name="NewPort" binding="tns:NewBinding">
			<soap:address location="No target address"/>
		</wsdl1:port>
	</wsdl1:service>
</wsdl1:definitions>'''
	def wsdlWithoutNsPrefix =
	'''<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://schemas.xmlsoap.org/wsdl/" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:http="http://schemas.xmlsoap.org/wsdl/http/" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/" xmlns:mime="http://schemas.xmlsoap.org/wsdl/mime/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:tns="http://new.webservice.namespace" targetNamespace="http://new.webservice.namespace">
	<types>
		<xs:schema targetNamespace="http://new.webservice.namespace" elementFormDefault="qualified"/>
	</types>
	<message name="NewMessageRequest">
		<part name="parameter" type="xs:string"/>
	</message>
	<message name="NewMessageResponse">
		<part name="parameter" type="xs:string"/>
	</message>
	<portType name="NewPortType">
		<operation name="NewOperation">
			<input message="tns:NewMessageRequest"/>
			<output message="tns:NewMessageResponse"/>
		</operation>
	</portType>
	<binding name="NewBinding" type="tns:NewPortType">
		<soap:binding style="document" transport="http://schemas.xmlsoap.org/soap/http"/>
		<operation name="NewOperation">
			<soap:operation soapAction="urn:#NewOperation"/>
			<input>
				<soap:body use="literal"/>
			</input>
			<output>
				<soap:body use="literal"/>
			</output>
		</operation>
	</binding>
	<service name="NewService">
		<port name="NewPort" binding="tns:NewBinding">
			<soap:address location="No target address"/>
		</port>
	</service>
</definitions>'''
	def parser = new WSDLParser();

	void testWsdlNameSpaceWithPrefix() {
		def loadedDefinitions = parser.parse(new ByteArrayInputStream(wsdlWithNsPrefix.bytes));
		//chances are failure will be at parse time
		def reparsedDefinitions = parser.parse(new ByteArrayInputStream(loadedDefinitions.getAsString().bytes));
		List<Difference> diffs = new WsdlDiffGenerator(loadedDefinitions, reparsedDefinitions).compare();
		assert diffs.size() == 0;
	}
	void testWsdlNameSpaceWithoutPrefix() {
		def loadedDefinitions = parser.parse(new ByteArrayInputStream(wsdlWithoutNsPrefix.bytes));
		//chances are failure will be at parse time
		def reparsedDefinitions = parser.parse(new ByteArrayInputStream(loadedDefinitions.getAsString().bytes));
		List<Difference> diffs = new WsdlDiffGenerator(loadedDefinitions, reparsedDefinitions).compare();
		assert diffs.size() == 0;
	}
}
