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

import org.apache.commons.logging.*

import com.predic8.soamodel.*
import com.predic8.wsdl.*
import com.predic8.schema.diff.*

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
			def typesDiffs = compareDocumentation(a.types, b.types)
			if(typesDiffs) diffs << new Difference(description:"Types has changed: ", breaks:false, safe: true,  diffs: typesDiffs)
//			diffs.addAll(compareSchemas())
			
			diffs.addAll(compareMessages())
			
			diffs.addAll(comparePortTypes())
			
			lDiffs.addAll(compareDocumentation(a.services[0], b.services[0]))
			if ( a.services[0] && b.services[0] ) {
				lDiffs.addAll(comparePorts())
			}
			if(lDiffs) diffs << new Difference(description:"Service ${a.services[0].name} has changed:", type : 'service', diffs: lDiffs)
		}
		if(diffs) return [new Difference(description:"Definitions has changed:", type : 'definitions', diffs: diffs)]
	}

	private def comparePorts(){
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
	
	private def compareMessages() {
		def aMessages = a.messages
		def bMessages = b.messages
		def diffs = []
		diffs.addAll(compare(aMessages,bMessages,
			{ new Difference(description:"Message ${it.name} removed.", breaks:true, safe:false) },
			{ new Difference(description:"Message ${it.name} added.", safe:true, breaks:false) }))
		def msg = aMessages.name.intersect(bMessages.name)
		msg.each{ msgName ->
			Message aMsg = aMessages.find{ it.name == msgName}
			Message bMsg = bMessages.find{ it.name == msgName}
			def lDiffs = compareDocumentation(aMsg, bMsg)
			//TODO compare Message Parts not implemented yet!
			if(lDiffs) diffs << new Difference(description:"Message $msgName has changed:", diffs : lDiffs)
		}
		diffs
	}

	private def comparePortTypes(){
		def aPortTypes = a.portTypes
		def bPortTypes = b.portTypes
		def diffs = []
		diffs.addAll( compare(aPortTypes, bPortTypes,
				{ new Difference(description:"PortType ${it.name} removed." , breaks:true) },
				{ new Difference(description:"PortType ${it.name} added." , safe:true) }))
		diffs.addAll(compareOperations(aPortTypes, bPortTypes))
		diffs
	}
	
	 //TODO at message.element the QNames have to be compared insted of prefixes.
	 
	 //TODO it is not implemented yet, if an operation changes the MEP.
	 
	private def compareOperations(aPortTypes, bPortTypes) {
		def diffs = []
		def lDiffs = []
		def ptNames = aPortTypes.name.intersect(bPortTypes.name)
		ptNames.each{ ptName ->
			lDiffs.addAll(compareDocumentation(aPortTypes.find{ it.name == ptName}, bPortTypes.find{ it.name == ptName}))
			
			def aOperations = aPortTypes.find{ it.name == ptName}.operations
			def bOperations = bPortTypes.find{ it.name == ptName}.operations
			
			lDiffs.addAll(compare(aOperations, bOperations,
					{ new Difference(description:"Operation ${it.name} removed.", breaks:true) },
					{ new Difference(description:"Operation ${it.name} added.", safe:true) }))
			def opNames = aOperations.name.intersect(bOperations.name)

			opNames.each{ opName ->
				def opDiffs = []
				def aOperation = aOperations.find{it.name == opName}
				def bOperation = bOperations.find{it.name == opName}

				opDiffs.addAll(compareDocumentation(aOperation, bOperation))
				
				if(aOperation.input.name != bOperation.input.name)
					opDiffs << new Difference(description:"The name of the input message changed from ${aOperation.input.name} to ${bOperation.input.name}.")
				if(aOperation.output && bOperation.output && aOperation.output.name != bOperation.output.name)
					opDiffs << new Difference(description:"The name of the output message changed from ${aOperation.output.name} to ${bOperation.output.name}.")

				def aInputMessage = aOperation.input.message
				def bInputMessage = bOperation.input.message
				log.debug("comparing input $aInputMessage with $bInputMessage")
				if(aOperation.output) def aOutputMessage = a.messages.find{it.name == aOperation.output.name}
				if(bOperation.output) def bOutputMessage = b.messages.find{it.name == bOperation.output.name}
				if(aInputMessage.parts[0]?.element != bInputMessage.parts[0]?.element) {
					opDiffs << new Difference(description:"In message ${aInputMessage.name} the schema element changed from ${aInputMessage.parts[0]?.element?:'empty'} to ${bInputMessage.parts[0]?.element?:'empty'}.", breaks:true)
				} else { 
					println aInputMessage.parts[0]?.element
					println bInputMessage.parts[0]?.element
				}
				if(opDiffs) lDiffs << new Difference(description:"Operation ${opName} has changed: " , type: 'operation' ,  diffs : opDiffs)
			}
			if(lDiffs) diffs << new Difference(description:"PortType ${ptName} has changed: " , type: 'portType' ,  diffs : lDiffs)
		}
		diffs
	}

	private def compareSchemas(){
		//TODO
		return
		def aSchemas = a.schemas
		def bSchemas = b.schemas
		def diffs = []

		def schemas = aSchemas.targetNamespace.intersect(bSchemas.targetNamespace)
		schemas.each{  tns ->
			def aSchema = aSchemas.find{it.targetNamespace == tns}
			def bSchema = bSchemas.find{it.targetNamespace == tns}
			log.debug("comparing schemas with namespace ${aSchema.targetNamespace}.")
			def schemaDiffGenerator = new SchemaDiffGenerator(a:aSchema, b:bSchema)
			def lDiffs = schemaDiffGenerator.compare()
			if(lDiffs) diffs << new Difference(description:"Schema ${tns ? tns+' ' : ''}has changed:" , diffs : lDiffs)
		}
		diffs
	}

	private def compareDocumentation(a,b){
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