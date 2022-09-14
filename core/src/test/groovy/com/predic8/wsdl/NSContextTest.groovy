package com.predic8.wsdl

import com.predic8.wsdl.ns.NSContext
import com.predic8.xml.util.ClasspathResolver

class NSContextTest extends GroovyTestCase {

  def parser = new WSDLParser(resourceResolver: new ClasspathResolver())

  void testDisabled() {
    NSContext.enable(false)
    assert namespaces() == [
      "ns0": "http://xmlns.oracle.com/adf/svc/types/",
      // value of last processed namespace with "ns1" prefix
      "ns1": "http://xmlns.oracle.com/apps/flex/prc/poz/suppliers/supplierServiceV2/supplierContact/"
    ]
  }

  void testEnabled() {
    NSContext.enable(true)
    assert namespaces() == [
      "ns0": "http://xmlns.oracle.com/adf/svc/types/",
      // has unique prefix for each processed namespace, that was defined with "ns1" prefix
      "ns1": "http://xmlns.oracle.com/apps/flex/prc/poz/suppliers/supplierServiceV2/supplier/",
      "ns2": "http://xmlns.oracle.com/apps/flex/prc/poz/suppliers/supplierServiceV2/supplierSites/",
      "ns3": "http://xmlns.oracle.com/apps/flex/prc/poz/suppliers/supplierServiceV2/supplierAddress/",
      "ns4": "http://xmlns.oracle.com/apps/flex/prc/poz/suppliers/supplierServiceV2/supplierContact/"
    ]
  }

  // returns namespaces of specific schema, that has issues with overrode namespaces
  def namespaces() {
    def wsdl = parser.parse("supplier-service-v2/SupplierServiceV2.wsdl")
    return wsdl.schemas[6].namespaces().findAll { it.key.startsWith("ns") }
  }
}
