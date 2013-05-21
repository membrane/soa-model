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

import com.predic8.schema.Element;
import com.predic8.schema.diff.*
import com.predic8.soamodel.*
import com.predic8.wsdl.*

class WsdlDiffGenerator extends AbstractDiffGenerator{

	private Log log = LogFactory.getLog(this.class)

	public WsdlDiffGenerator(){
	}

	public WsdlDiffGenerator(Definitions a, Definitions b){
		this.a = a
		this.b = b
	}

	List<Difference> compare() {
		def diffs = []
		def lDiffs = []
		diffs.addAll(compareDocumentation(a, b))
		if ( a.targetNamespace != b.targetNamespace )
			diffs << new Difference(description:"TargetNamespace changed from ${a.targetNamespace} to ${b.targetNamespace}", breaks:true)

		if ( a.services[0] && b.services[0] && a.services[0].name != b.services[0].name )
			diffs << new Difference(description:"Servicename changed from ${a.services[0].name} to ${b.services[0].name}", breaks:true)
		else {
			diffs.addAll(compareTypes())

			diffs.addAll(compareMessages())

			diffs.addAll(comparePortTypes())

			lDiffs.addAll(compareDocumentation(a.services[0], b.services[0]))
			if ( a.services[0] && b.services[0] ) {
				lDiffs.addAll(comparePorts())
			}
			if(lDiffs) diffs << new Difference(description:"Service ${a.services[0].name} has changed:", type : 'service', diffs: lDiffs)
		}
		if(diffs) return [
				new Difference(description:"Definitions has changed:", type : 'definitions', diffs: diffs)
			]
		[]
	}

	private List<Difference> compareTypes(){
		def diffs = compareDocumentation(a.types, b.types)
		//		def lDiffs = compareSchemas()
		//		if(lDiffs) diffs << new Difference(description:"Types has changed: ", breaks:false, safe: true,  diffs: lDiffs)
		diffs
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
			if(lDiffs) diffs << new Difference(description:"Port $portName has changed:", diffs : lDiffs)
			if(aPort.address.location != bPort.address.location)
				diffs << new Difference(description:"The location of the port $portName changed form ${aPort.address.location} to ${bPort.address.location}.", breaks:true, safe:false)
		}
		diffs
	}

	private List<Difference> compareMessages() {
		def aMessages = a.messages
		def bMessages = b.messages
		def diffs = []
		diffs.addAll(compare(aMessages,bMessages,
				{ new Difference(description:"Message ${it.name} removed.", breaks:true, safe:false) },
				{ new Difference(description:"Message ${it.name} added.", safe:true, breaks:false) }))
		//		def msg = aMessages.name.intersect(bMessages.name)
		//		msg.each{ msgName ->
		//			//TODO use compareMessage() instead.
		//			Message aMsg = aMessages.find{ it.name == msgName}
		//			Message bMsg = bMessages.find{ it.name == msgName}
		//			def lDiffs = compareDocumentation(aMsg, bMsg)
		//			if(lDiffs) diffs << new Difference(description:"Message $msgName has changed:", diffs : lDiffs)
		//		}
		diffs
	}

	private List<Difference> comparePortTypes(){
		def aPortTypes = a.portTypes
		def bPortTypes = b.portTypes
		def diffs = []
		diffs.addAll( compare(aPortTypes, bPortTypes,
				{ new Difference(description:"PortType ${it.name} removed." , breaks:true) },
				{ new Difference(description:"PortType ${it.name} added." , safe:true) }))

		def ptNames = aPortTypes.name.intersect(bPortTypes.name)
		ptNames.each{ ptName ->
			PortType aPT = aPortTypes.find{ it.name == ptName}
			PortType bPT = bPortTypes.find{ it.name == ptName}
			diffs.addAll(comparePortType(aPT, bPT))
		}
		diffs
	}

	private List<Difference> comparePortType(aPT, bPT){
		def diffs = compareDocumentation(aPT, bPT)
		diffs.addAll(compareOperations(aPT.operations, bPT.operations))
		if(diffs) return [
				new Difference(description:"PortType ${aPT.name} has changed: " , type: 'portType' ,  diffs : diffs)
			]
		[]
	}

	private List<Difference> compareOperations(aOperations, bOperations) {
		def diffs = []
		diffs.addAll(compare(aOperations, bOperations,
				{ new Difference(description:"Operation ${it.name} removed.", breaks:true) },
				{ new Difference(description:"Operation ${it.name} added.", safe:true) }))

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
		if(aOperation.input.name == bOperation.input.name) {
			diffs.addAll(comparePortTypeMessage(aOperation.input, bOperation.input))
		} else {
			diffs << new Difference(description:"Input name has changed from ${aOperation.input.name} to ${bOperation.input.name}.", type:'operation', breaks : true)
		}
		if(aOperation.output?.name == bOperation.output?.name) {
			diffs.addAll(comparePortTypeMessage(aOperation.output, bOperation.output))
		} else {
			diffs << new Difference(description:"Output name has changed from ${aOperation.output.name} to ${bOperation.output.name}.", type:'operation', breaks : true)
		}
		diffs.addAll(compareFaults(aOperation.faults, bOperation.faults))

		if(diffs) return [
				new Difference(description:"Operation ${aOperation.name} has changed: ", type: 'operation', diffs: diffs)
			]
		[]
	}

	//Compare operation input/output/fault
	private List<Difference> comparePortTypeMessage(aPTM, bPTM) {
		if(!aPTM && !bPTM) return []
		if(aPTM && !bPTM) return [
				new Difference(description:"${getElementTagName(aPTM)} removed.")
			]
		if(!aPTM && bPTM) return [
				new Difference(description:"${getElementTagName(bPTM)} added.")
			]
		def diffs = compareDocumentation(aPTM, bPTM)
		if(aPTM.message.name != bPTM.message.name || aPTM.message.namespaceUri != bPTM.message.namespaceUri) diffs << new Difference(description: "${getElementTagName(aPTM)} message has changed from ${aPTM.message.prefix}:${aPTM.message.name} to ${bPTM.message.prefix}:${bPTM.message.name}", type: 'message', breaks : true)
		else diffs.addAll(compareMessage(aPTM.message, bPTM.message))
		if(diffs) return [
				new Difference(description:"${getElementTagName(aPTM)} has changed:", diffs: diffs)
			]
		[]
	}

	private String getElementTagName(WSDLElement we) {
		we.elementName.localPart.capitalize()
	}

	//TODO
	private List<Difference> compareFaults(aFaults, bFaults) {
		[]
	}

	protected List<Difference> compareMessage(Message a, Message b) {
		def diffs = compareDocumentation(a, b)
		diffs.addAll( compareParts(a.parts, b.parts))
		if(diffs) return [
				new Difference(description:"Message ${a.name} has changed: ", type: 'message', diffs : diffs)
			]
		[]
	}

	private List<Difference> compareParts(aParts, bParts) {
		def diffs = []
		diffs.addAll( compare(aParts, bParts,
				{ new Difference(description:"Part ${it.name} removed." , breaks:true) },
				{ new Difference(description:"Part ${it.name} added." , breaks:true) }))
		def partNames = aParts.name.intersect(bParts.name)
		partNames.each{ ptName ->
			Part a = aParts.find{ it.name == ptName}
			Part b = bParts.find{ it.name == ptName}
			diffs.addAll(comparePart(a, b))
		}
		diffs
	}


	private List<Difference> comparePart(Part a, Part b) {
		def diffs = compareDocumentation(a, b)
		Element aElement = a.element
		Element bElement = b.element
		if(aElement && b.type) 
			diffs << new Difference(description:"Element ${aElement.name} has changed to type ${b.type}", type:'part', breaks : true)
		else if(bElement && a.type)
			diffs << new Difference(description:"Type ${a.type} has changed to element ${bElement.name}", type:'part', breaks : true)
		else if(aElement?.name != bElement?.name || aElement.namespaceUri != bElement.namespaceUri)
			diffs << new Difference(description:"Element has changed from ${aElement.name} to ${bElement.name}", type:'part', breaks : true)
		else {
			def generator = new SchemaDiffGenerator()

			def aT = aElement.schema.getType(aElement.type)
			def bT = bElement.schema.getType(bElement.type)
			if(aT == bT) {
				def lDiffs = []
				lDiffs.addAll(new ComplexTypesDiffGenerator(a: [aT], b: [bT], generator : generator).compare())
				if(lDiffs) diffs << new Difference(description:"Element ${aElement.name} has changed:}", type:'element', diffs : lDiffs)
			} else {
				diffs.addAll(new ElementDiffGenerator(a: aElement , b: bElement, generator: generator).compare())
			}
		}
		if(diffs) return [
				new Difference(description:"Part ${a.name} has changed: ", type: 'part', diffs : diffs)
			]
		[]
	}

	/*
	 * WsdlDiffGenerator doesn't compare all schema elements but the used one.
	 * So compareSchema() is not really needed!
	 */
	//	private List<Difference> compareSchemas(){
	//		def aSchemas = a.schemas
	//		def bSchemas = b.schemas
	//		def diffs = []
	//
	//		def schemas = aSchemas.targetNamespace.intersect(bSchemas.targetNamespace)
	//		schemas.each{  tns ->
	//			def aSchema = aSchemas.find{it.targetNamespace == tns}
	//			def bSchema = bSchemas.find{it.targetNamespace == tns}
	//			log.debug("comparing schemas with namespace ${aSchema.targetNamespace}.")
	//			def schemaDiffGenerator = new SchemaDiffGenerator(a:aSchema, b:bSchema)
	//			def lDiffs = schemaDiffGenerator.compare()
	//			if(lDiffs) diffs << new Difference(description:"Schema ${tns ? tns+' ' : ''}has changed:" , diffs : lDiffs)
	//		}
	//		diffs
	//	}

	protected List<Difference> compareDocumentation(a,b){
		if(a.documentation && !b.documentation) return [
				new Difference(description:"Documentation removed.", breaks : false, safe : true)
			]
		if(!a.documentation && b.documentation) return [
				new Difference(description:"Documentation added.", breaks : false, safe : true)
			]
		if(getNormalizedContent(a.documentation?.content) != getNormalizedContent(b.documentation?.content))
			return [
				new Difference(description:"Documentation has changed.", breaks : false, safe : true)
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