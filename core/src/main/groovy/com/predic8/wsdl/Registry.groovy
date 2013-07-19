package com.predic8.wsdl

class Registry {

	def wsdls = [:]

	void add(Definitions definitions) {
		if(wsdls[definitions.targetNamespace]) wsdls[definitions.targetNamespace] << definitions
		else wsdls[definitions.targetNamespace] = [definitions]
	}

	List<Definitions> getWsdls(String ns) {
		wsdls[ns]
	}
	
	List<Definitions> getAllWsdls() {
		wsdls.values().flatten().unique()
	}
	
	String toString() {
		wsdls.toString()
	}
}
