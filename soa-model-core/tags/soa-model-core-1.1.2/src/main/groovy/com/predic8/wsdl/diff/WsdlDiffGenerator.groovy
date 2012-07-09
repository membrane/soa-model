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
  
  public WsdlDiffGenerator(){}
  
  public WsdlDiffGenerator(Definitions a, Definitions b){
    this.a = a
    this.b = b
  }
  
  List<Difference> compare() {
    def diffs=[]
    
    if ( a.targetNamespace != b.targetNamespace )
      diffs << new Difference(description:"targetNamespace changed from ${a.targetNamespace} to ${b.targetNamespace}", breaks:true)
    if ( a.services[0] && b.services[0] && a.services[0].name != b.services[0].name )
      diffs << new Difference(description:"servicename changed from ${a.services[0].name} to ${b.services[0].name}", breaks:true)
    else {
      if ( a.services[0] && b.services[0] ) {
        diffs.addAll(comparePorts())
      }
      diffs.addAll(comparePortTypes())
      diffs.addAll(compareSchemas())
    }
    diffs
  }
  
  private def comparePorts(){
    def aPorts = a.services[0].ports
    def bPorts = b.services[0].ports
    def diffs = []
    diffs.addAll(compare(aPorts, bPorts, 
      { new Difference(description:"Port ${it.name} removed.", breaks:true) },
      { new Difference(description:"Port ${it.name} added.", safe:true) }))
    def ports = aPorts.name.intersect(bPorts.name)
    ports.each{ port -> 
      def aLocation = aPorts.find{ it.name == port}.address.location
      def bLocation = bPorts.find{ it.name == port}.address.location
      if(aLocation != bLocation)
        diffs << new Difference(description:"The location of the port: $port changed form $aLocation to $bLocation.")
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
  /*
   * @TODO at message.element the QNames have to be compared insted of prefixes.
   *
   * @TODO it is not implemented yet, if an operation changes the MEP.
   */
  private def compareOperations(aPortTypes, bPortTypes) {
    def diffs = []
    def ptNames = aPortTypes.name.intersect(bPortTypes.name)
    ptNames.each{ ptName -> 
      def aOperations = a.portTypes.find{ it.name == ptName}.operations
      def bOperations = b.portTypes.find{ it.name == ptName}.operations
      diffs.addAll(compare(aOperations, bOperations,
      { new Difference(description:"Operation ${it.name} removed.", breaks:true) },
      { new Difference(description:"Operation ${it.name} added.", safe:true) }))
      def opNames = aOperations.name.intersect(bOperations.name)
      
      opNames.each{ opName -> 
        def aOperation = aOperations.find{it.name == opName}
        def bOperation = bOperations.find{it.name == opName}
        
        if(aOperation.input.name != bOperation.input.name)
          diffs << new Difference(description:"In operation ${aOperation.name} of portType $ptName the name of the input message changed from ${aOperation.input.name} to ${bOperation.input.name}.")
        if(aOperation.output && bOperation.output && aOperation.output.name != bOperation.output.name)
          diffs << new Difference(description:"In operation ${aOperation.name} of portType $ptName the name of the output message changed from ${aOperation.output.name} to ${bOperation.output.name}.")
        
        def aInputMessage = aOperation.input.message
        def bInputMessage = bOperation.input.message
        log.debug("comparing input $aInputMessage with $bInputMessage")
        if(aOperation.output) def aOutputMessage = a.messages.find{it.name == aOperation.output.name}
        if(bOperation.output) def bOutputMessage = b.messages.find{it.name == bOperation.output.name}
        if(aInputMessage.parts[0]?.element != bInputMessage.parts[0]?.element) {
          diffs << new Difference(description:"In message ${aInputMessage.name} of operation $opName the schema element changed from ${aInputMessage.parts[0]?.element?:'empty'} to ${bInputMessage.parts[0]?.element?:'empty'}.", breaks:true)
        }
      }
    }
    diffs
  }
  
  private def compareSchemas(){
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
      if(lDiffs) {
        diffs << new Difference(description:"Schema ${tns} has changed:" , diffs : lDiffs)
      }
    }
    diffs
  }
}