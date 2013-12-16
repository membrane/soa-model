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

package com.predic8.wsdl.diff

import groovy.xml.QName

import org.apache.commons.logging.*

import com.predic8.schema.ComplexType;
import com.predic8.schema.Element;
import com.predic8.schema.diff.*
import com.predic8.soamodel.*
import com.predic8.wsdl.*

class WsdlDiffGenerator extends AbstractDiffGenerator{

	private Log log = LogFactory.getLog(this.class)

	public WsdlDiffGenerator(){	}

	public WsdlDiffGenerator(Definitions a, Definitions b){
		this.a = a
		this.b = b
	}

	List<Difference> compare() {
		def diffs = []
		def lDiffs = []
		
		if( a.targetNamespace != b.targetNamespace )
			diffs << new Difference(description:"TargetNamespace changed from ${a.targetNamespace} to ${b.targetNamespace}.", breaks:true)

		if( a.services[0] && b.services[0] && a.services[0].name != b.services[0].name )
			diffs << new Difference(description:"Servicename changed from ${a.services[0].name} to ${b.services[0].name}.", breaks:false)
		
		diffs.addAll(compareDocumentation(a, b))

		/* Do not change the order of comparePortTypes and compareTypes!
		 * Exchange information for schema elements would be available
		 * after comparing portTypes. 
		 */
		diffs.addAll(comparePortTypes())
		
		diffs.addAll(compareTypes())

		lDiffs.addAll(compareDocumentation(a.services[0], b.services[0]))
		if ( a.services[0] && b.services[0] ) {
			lDiffs.addAll(comparePorts())
		}
		if(lDiffs) diffs << new Difference(description:"Service ${a.services[0].name}:", type : 'service', diffs: lDiffs)
		
		if(diffs) return [new Difference(description:"Definitions:", type : 'definitions', diffs: diffs)]
		[]
	}

	private List<Difference> compareTypes(){
		def lDiffs = compareDocumentation(a.localTypes, b.localTypes)
		lDiffs.addAll(compareSchemas())
		if(lDiffs) return [new Difference(description:"Types: ", breaks:false,  diffs: lDiffs, type: 'types')]
		[]
	}

	private List<Difference> comparePorts(){
		def aPorts = a.services[0].ports
		def bPorts = b.services[0].ports
		def diffs = []
		diffs.addAll(compare(aPorts, bPorts,
				{ new Difference(description:"Port ${it.name} removed.", breaks:true, safe:false) },
				{ new Difference(description:"Port ${it.name} added.", safe:true, breaks:false) }))
		def ports = aPorts.name.intersect(bPorts.name)
		ports.each{ portName ->
			Port aPort = aPorts.find{ it.name == portName}
			Port bPort = bPorts.find{ it.name == portName}
			def lDiffs = compareDocumentation(aPort, bPort)
			if(lDiffs) diffs << new Difference(description:"Port $portName:", diffs : lDiffs)
			if(aPort.address.location != bPort.address.location)
				diffs << new Difference(description:"The location of the port $portName changed form ${aPort.address.location} to ${bPort.address.location}.", breaks:true, safe:false)
		}
		diffs
	}

	private List<Difference> comparePortTypes(){
		def aPortTypes = a.portTypes
		def bPortTypes = b.portTypes
		def diffs = []
		if(aPortTypes.size() == 1 && bPortTypes.size() == 1) {
			if(aPortTypes[0].name != bPortTypes[0].name) {
				diffs = comparePortType(aPortTypes[0], bPortTypes[0])
				if(diffs) return [
					new Difference(description:"PortType name has changed from ${aPortTypes[0].name} to ${bPortTypes[0].name}:" , type: 'portType' ,  diffs : diffs)
				]
				return [new Difference(description:"PortType name has changed from ${aPortTypes[0].name} to ${bPortTypes[0].name}." , type: 'portType')] 
			}
		}
		else {
			diffs.addAll( compare(aPortTypes, bPortTypes,
					{ new Difference(description:"PortType ${it.name} removed." , breaks:true, type:'portType') },
					{ new Difference(description:"PortType ${it.name} added." , safe:true, type:'portType') }))
		}
		def ptNames = aPortTypes.name.intersect(bPortTypes.name)
		ptNames.each{ ptName ->
			PortType aPT = aPortTypes.find{ it.name == ptName}
			PortType bPT = bPortTypes.find{ it.name == ptName}
			diffs.addAll(comparePortType(aPT, bPT))
		}
		if(diffs) return [
			new Difference(description:"PortType ${aPortTypes[0].name}:" , type: 'portType' ,  diffs : diffs)
		]
		[]
	}

	private List<Difference> comparePortType(aPT, bPT){
		def diffs = compareDocumentation(aPT, bPT)
		diffs.addAll(compareOperations(aPT.operations, bPT.operations))
		diffs
	}

	private List<Difference> compareOperations(aOperations, bOperations) {
		def diffs = []
		diffs.addAll(compare(aOperations, bOperations,
				{ new Difference(description:"Operation ${it.name} removed.", original: it, breaks:true, type:'operation') },
				{ new Difference(description:"Operation ${it.name} added.", modified: it, safe:true, type:'operation') }))

		def opNames = aOperations.name.intersect(bOperations.name)
		opNames.each{ opName ->
			//TODO Test if input/output name matches.
			Operation aOperation = aOperations.find{it.name == opName}
			Operation bOperation = bOperations.find{it.name == opName}
			diffs.addAll(compareOperation(aOperation, bOperation))
		}
		diffs
	}

	//TODO it is not implemented yet, if an operation changes the MEP.

	private List<Difference> compareOperation(aOperation, bOperation) {
		def diffs = compareDocumentation(aOperation, bOperation)
		diffs.addAll(comparePortTypeMessage(aOperation.input, bOperation.input, 'input'))
		diffs.addAll(comparePortTypeMessage(aOperation.output, bOperation.output, 'output'))
		diffs.addAll(compareFaults(aOperation.faults, bOperation.faults, ['fault']))
		if(diffs) return [
				new Difference(description:"Operation ${aOperation.name}:", original:aOperation, modified:bOperation, type:'operation', diffs: diffs)
			]
		[]
	}

	//Compare operation input/output/fault
	private List<Difference> comparePortTypeMessage(aPTM, bPTM, ptmName) {
		def exchange
		switch (ptmName) {
			case "input" : exchange = ['request'] ; break
			case "output" : exchange = ['response'] ; break
			case "fault" : exchange = ['fault'] ; break 
		}
		if(!aPTM && !bPTM) return []
		if(aPTM && !bPTM) return [new Difference(description:"${ptmName.capitalize()} removed.", exchange:exchange.clone(), type: ptmName)]
		if(!aPTM && bPTM) return [new Difference(description:"${ptmName.capitalize()} added.", exchange:exchange.clone(), type: ptmName)]
		def lDiffs = compareDocumentation(aPTM, bPTM)
		if(aPTM.name != bPTM.name) {
			lDiffs << new Difference(description:"Name has changed from ${aPTM.name} to ${bPTM.name}.", type:'input', breaks : false, exchange:['request'])
		}
		if(aPTM.message.name != bPTM.message.name || aPTM.message.namespaceUri != bPTM.message.namespaceUri) 
			lDiffs << new Difference(description: "Message has changed from ${aPTM.message.qname} to ${bPTM.message.qname}.", type: ptmName, breaks : false, exchange:exchange.clone())
		lDiffs.addAll(compareMessage(aPTM.message, bPTM.message, exchange))
		if(lDiffs) return [ new Difference(description:"${ptmName.capitalize()}:", diffs: lDiffs, exchange:exchange.clone(), type: ptmName)]
		lDiffs
		[]
	}

	private List<Difference> compareFaults(aFaults, bFaults, exchange) {
		def diffs = []
		def faults = aFaults.message.qname.intersect(bFaults.message.qname)
		(aFaults.message.qname - faults).each {
			diffs << new Difference(description:"Fault with message ${it} removed.", type: 'fault', exchange:exchange.clone())
		}
		(bFaults.message.qname - faults).each {
			diffs << new Difference(description:"Fault with message ${it} added.", type: 'fault', exchange:exchange.clone())
		}
		faults.each { f ->
			diffs.addAll(comparePortTypeMessage(aFaults.find{it.message.name == f}, bFaults.find{it.message.name == f}, exchange))
		}
		diffs
	}

	protected List<Difference> compareMessage(Message a, Message b, exchange) {
		def diffs = compareDocumentation(a, b)
		diffs.addAll( compareParts(a.parts, b.parts, exchange))
		if(diffs) return [
				new Difference(description:"Message${(a.name == b.name)? ' '+a.name : ''}:", type: 'message', diffs : diffs, exchange:exchange.clone())
			]
		[]
	}

	private List<Difference> compareParts(aParts, bParts, exchange) {
		def diffs = []
		diffs.addAll( compare(aParts, bParts,
				{ new Difference(description:"Part ${it.name} removed." , breaks:true, exchange:exchange.clone()) },
				{ new Difference(description:"Part ${it.name} added." , breaks:true, exchange:exchange.clone()) }))
		def partNames = aParts.name.intersect(bParts.name)
		partNames.each{ ptName ->
			Part a = aParts.find{ it.name == ptName}
			Part b = bParts.find{ it.name == ptName}
			diffs.addAll(comparePart(a, b, exchange))
		}
		diffs
	}


	private List<Difference> comparePart(Part a, Part b, exchange) {
		def diffs = compareDocumentation(a, b)
		if(a.elementPN && b.typePN) {
			a.element?.exchange = exchange.clone()
			b.type?.exchange = exchange.clone()
			diffs << new Difference(description:"Element ${a.elementPN} has changed to type ${b.typePN}.", type:'element2type', breaks : true, exchange:exchange.clone())
		}
		else if(b.elementPN && a.typePN) {
			a.type?.exchange = exchange.clone()
			b.element?.exchange = exchange.clone()
			diffs << new Difference(description:"Type ${a.typePN} has changed to element ${b.elementPN}.", type:'type2element', breaks : true, exchange:exchange.clone())
		}
		else if(a.typePN && b.typePN) {
			try {
				a.type.exchange = exchange.clone()
				b.type.exchange = exchange.clone()
			} catch (Exception e) {
				return [new Difference(description:"Part ${a.name} uses an invalid type:", type: 'part', exchange:exchange.clone(),
					diffs : [new Difference(description:e.message, type:'type', breaks : true, exchange:exchange.clone())]
					)]
			}
			//CompareComplexType does NOT detect if a CT has changed only the namespaceURI! So the next line is needed.
			if(a.type.qname != b.type.qname){
				diffs << new Difference(description:"Type has changed from ${a.type.qname} to ${b.type.qname}.",
					type:'type', breaks : true, exchange:exchange.clone())
			}
			else diffs.addAll(a.type.compare(new SchemaDiffGenerator(compare4WSDL:true), b.type))
		}
		else if(a.elementPN && b.elementPN) {
			try {
				a.element.exchange = exchange.clone()
			} catch (Exception e) {
				return [new Difference(description:"Part ${a.name} in the original document uses an invalid element('${a.elementPN}'):", type: 'part', exchange:exchange.clone(),
					diffs : [new Difference(description:e.message, type:'element', breaks : true, exchange:exchange.clone())]
					)]
			}
			try {
				b.element.exchange = exchange.clone()
			} catch (Exception e) {
				return [new Difference(description:"Part ${b.name} in the modified document uses an invalid element('${b.elementPN}'):", type: 'part', exchange:exchange.clone(),
					diffs : [new Difference(description:e.message, type:'element', breaks : true, exchange:exchange.clone())]
					)]
			}
			if(a.elementPN.localName != b.elementPN.localName){
				diffs << new Difference(description:"Element has changed from ${a.elementPN} to ${b.elementPN}.",
					type:'element', breaks : true, exchange:exchange.clone())
			}
			else if(a.getNamespace(a.elementPN.prefix) != b.getNamespace(b.elementPN.prefix)) {
				diffs << new Difference(description:"Element has changed from ${a.element.qname} to ${b.element.qname}.", 
					type:'element', breaks : true, exchange:exchange.clone())
			}
			else if(a.element && b.element) {
				diffs.addAll(new ElementDiffGenerator(a:a.element, b:b.element, generator:new SchemaDiffGenerator(compare4WSDL:true)).compare())
			}
		}
		if(diffs) return [new Difference(description:"Part ${a.name}:", type: 'part', diffs : diffs, exchange:exchange.clone())]
		[]
	}

	/*
	 * WsdlDiffGenerator doesn't compare all schema elements but the used one.
	 * So compareSchema() is not really needed!
	 */
		private List<Difference> compareSchemas(){
			def aSchemas = a.localSchemas
			def bSchemas = b.localSchemas
			def diffs = []
			(aSchemas.targetNamespace - bSchemas.targetNamespace).each { tns ->
				diffs << new Difference(description:"Schema ${tns ? tns+' ' : ''}removed." , type: 'schema')
			}
			
			(bSchemas.targetNamespace - aSchemas.targetNamespace).each { tns ->
				diffs << new Difference(description:"Schema ${tns ? tns+' ' : ''}added." , type: 'schema')
			}
			
			def schemas = aSchemas.targetNamespace.intersect(bSchemas.targetNamespace)
			schemas.each{  tns ->
				def aSchema = aSchemas.find{it.targetNamespace == tns}
				def bSchema = bSchemas.find{it.targetNamespace == tns}
				log.debug("comparing schemas with namespace ${aSchema.targetNamespace}.")
				def schemaDiffGenerator = new SchemaDiffGenerator(a:aSchema, b:bSchema)
				def lDiffs = schemaDiffGenerator.compare()
				if(lDiffs) diffs << new Difference(description:"Schema ${tns ? tns+' ' : ''}has changed:" , diffs : lDiffs, type: 'schema')
			}
			diffs
		}

	protected List<Difference> compareDocumentation(a,b){
		if(a?.documentation && !b?.documentation) return [
				new Difference(description:"Documentation removed.", breaks : false, safe : true, type: 'documentation')
			]
		if(!a?.documentation && b?.documentation) return [
				new Difference(description:"Documentation added.", breaks : false, safe : true, type: 'documentation')
			]
		if(getNormalizedContent(a?.documentation?.content) != getNormalizedContent(b?.documentation?.content))
			return [
				new Difference(description:"Documentation has changed.", breaks : false, safe : true, type: 'documentation')
			]
		[]
	}

	def getNormalizedContent(String content){
		if(!content) return
		content.replaceAll("\\s+", " ").trim()
	}

	protected def updateLabels(){
		labelTN = AbstractDiffGenerator.bundle.getString("com.predic8.wsdl.diff.labelTN")
		labelTo = AbstractDiffGenerator.bundle.getString("com.predic8.wsdl.diff.labelTo")
	}
}