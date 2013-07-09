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

import javax.xml.stream.*

import com.predic8.wsdl.soap11.SOAPBinding as Soap11Binding
import com.predic8.xml.util.ClasspathResolver

class ServiceTest extends AbstractWSDLTest {
  
  def service
  
  def static wsdl = '''<wsdl:definitions xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/" 
                                         xmlns="http://schemas.xmlsoap.org/wsdl/" 
                                         xmlns:wsaw="http://www.w3.org/2006/05/addressing/wsdl" 
                                         xmlns:tns="http://thomas-bayer.com/blz/" 
                                         xmlns:http="http://schemas.xmlsoap.org/wsdl/http/" 
                                         xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
                                         xmlns:mime="http://schemas.xmlsoap.org/wsdl/mime/" 
                                         xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" 
                                         xmlns:soap12="http://schemas.xmlsoap.org/wsdl/soap12/" 
                                         name="BLZService" 
                                         targetNamespace="http://thomas-bayer.com/blz/">
    <wsdl:service name="BLZService">
          <wsdl:port name="BLZServiceSOAP11port_http" binding="tns:BLZServiceSOAP11Binding">
              <soap:address location="http://www.thomas-bayer.com:80/axis2/services/BLZService"/>
          </wsdl:port>
      </wsdl:service>
  </wsdl:definitions>'''
  
  void setUp() {
		super.setUp()
		def binding = new Binding(name:'BLZServiceSOAP11Binding', binding : new Soap11Binding())
		definitions.localBindings << binding
		service = definitions.services[0]
  }
	
  void testBinding() {
    assert service.ports.binding 
    assert service.ports[0].binding.name == 'BLZServiceSOAP11Binding'
  }
  
  void testService() {
    assert  service.name == 'BLZService'
  }
  
  void testPort() {
    assert service.ports[0].name == 'BLZServiceSOAP11port_http' 
    assert service.ports[0].address.location == "http://www.thomas-bayer.com:80/axis2/services/BLZService" 
  }
}
