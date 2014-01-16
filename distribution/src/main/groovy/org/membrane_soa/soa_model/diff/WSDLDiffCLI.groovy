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

package org.membrane_soa.soa_model.diff

import groovy.xml.MarkupBuilder

import javax.xml.transform.*

import com.predic8.schema.Element
import com.predic8.schema.Schema
import com.predic8.schema.creator.*
import com.predic8.soamodel.Difference
import com.predic8.wsdl.*
import com.predic8.wsdl.diff.WsdlDiffGenerator

class WSDLDiffCLI extends AbstractDiffCLI{

	public static void main(String[] args) {
		WSDLDiffCLI diffCLI = new WSDLDiffCLI()
		diffCLI.start(args)
		//		diffCLI.createOperationPages()
	}

	void Diff2Xml(List<Difference> diffs){
		def writer = new ByteArrayOutputStream()
		builder = new MarkupBuilder(new PrintWriter(writer))
		builder.WSDLDiff{
			"Original"{
				URL(fixURL(url1))
				TargetNamespace(doc1.targetNamespace)
				Operations{
					doc1.operations.each { Operation(it.name) }
				}
				Services{
					doc1.services.each { service ->
						Service(service.name)
					}
				}
				Bindings{
					doc1.bindings.each {  Binding(it.name) }
				}
				PortTypes{
					doc1.portTypes.each { PortType(it.name) }
				}
				Messages{
					doc1.messages.each { Message(it.name) }
				}
			}
			"Modified"{
				URL(fixURL(url2))
				TargetNamespace(doc2.targetNamespace)
				Operations{
					doc2.operations.each { Operation(it.name) }
				}
				Services{
					doc2.services.each { service ->
						Service(service.name)
					}
				}
				Bindings{
					doc2.bindings.each { Binding(it.name) }
				}
				PortTypes{
					doc2.portTypes.each { PortType(it.name) }
				}
				Messages{
					doc2.messages.each { Message(it.name) }
				}
			}
			def jointPortTypes = doc1.portTypes.name.intersect(doc2.portTypes.name)
			def operationPages = []
			jointPortTypes.each {ptName ->
				OperationChangesInPortType('name':ptName){
					def (aPt, bPt) = [
						doc1.getPortType(ptName),
						doc2.getPortType(ptName)
					]
					def jointOpNames = aPt.operations.name.intersect(bPt.operations.name)
					jointOpNames.each { opName ->
						Operation('name':opName){
							def aOp = aPt.getOperation(opName)
							def bOp = bPt.getOperation(opName)
							Difference change = findOperationChanges(diffs, aOp)
							createOperation(builder, aOp, change)
							if(change?.diffs?.find{it.type == 'input'}) operationPages << [ptName, aOp, bOp, change, 'input']
							if(change?.diffs?.find{it.type == 'output'}) operationPages << [ptName, aOp, bOp, change, 'output']
							//TODO operationPage for faults
						}
					}
					(aPt.operations.name - jointOpNames).each {opName ->						//removed operations
						Operation('name':opName){
							removed()
							createOperation(builder, aPt.getOperation(opName), null)
						}
					}
					(bPt.operations.name - jointOpNames).each {opName ->						//added operations
						Operation('name':opName){
							added()
							createOperation(builder, bPt.getOperation(opName), null)
						}
					}
				}
			}
			(doc1.portTypes.name - jointPortTypes).each { RemovedPortType('name':it) }
			(doc2.portTypes.name - jointPortTypes).each { AddedPortType('name':it) }
			Diffs{
				diffs.each{ diff -> dump(diff) }
			}
			operationPages.each { createOperationPage(it) }
		}

		new File(reportFolder).mkdir()
		File xml = new File("$reportFolder/diff-report.xml")
		OutputStream outputStream = new FileOutputStream (xml);
		writer.writeTo(outputStream);
		transform(new ByteArrayInputStream(writer.toByteArray()), 'html')
	}

	private createOperation(MarkupBuilder builder, Operation op, Difference change) {
		def inputDiff = change?.diffs?.find{it.type == 'input'}
		builder.input(message: op.input?.message?.name , compatibility: computeCompatibility(inputDiff))
		def outputDiff = change?.diffs?.find{it.type == 'output'}
		builder.output(message: op.output?.message?.name , compatibility: computeCompatibility(outputDiff))
		//TODO Faults are missing!
	}

	private computeCompatibility(Difference diff){
		if(!diff) return ''
		diff.safe() ? 'safe' : (diff.breaks() ? 'breaking' : 'unsafe')
	}

	private findOperationChanges(diffs, op) {
		for(def diff in diffs) {
			if(diff.original == op | diff.modified == op){
				return diff
			}
		}
		if(diffs.diffs) findOperationChanges(diffs.diffs.flatten(), op)
	}

	private createOperationPage(String ptName, Operation aOp, Operation bOp,Difference opChange, String exchange) {
		def opName = aOp.name ?: bOp.name
		new File("$reportFolder/$ptName/").mkdirs()
		def writer = new FileWriter(new File("$reportFolder/$ptName/$opName-${exchange}.html"))
		builder = new MarkupBuilder(writer)
		builder.html(xmlns:"http://www.w3.org/1999/xhtml",lang:"en",'xml:lang':"en"){
			head {
				title  "$opName($exchange)"
				link(rel: "stylesheet", type:"text/css", href:"../web/a.css")
				link(rel: "stylesheet", href: "../web/jquery.treeview.css" )
				script( '', type: "text/javascript", src: "../web/jquery.js")
				script( '', type:"text/javascript", src:"../web/jquery.treeview.js")
				script( 'jQuery(document).ready(function(){jQuery("#diffs").treeview();});', type: "text/javascript")
				script('' , src:"../web/run_prettify.js")
			}
			body {
				div('class' : "container") {
					h1 "${exchange.capitalize()} Message of Operation '$opName'"
					if(!opChange|| !opChange.diffs.type.contains(exchange)) h2 'There are no differences in this view.'
					else {
						h2 "Differences:"
						div {
							opChange.diffs.findAll {it.type == exchange}.each { diff ->  dumpOperationDiff(diff) }
						}
					}


					h2 "${exchange.capitalize()} Template"
					p '''The message templates below show how an original message
									and a message based on the modified WSDL will look like.'''
					table('class' : "table table-striped table-bordered")  {
						thead{
							tr{
								th('Original')
								th('Modified')
							}
						}
						tbody{
							tr{
								td(width:"50%"){
									Element element = doc1.getElementforOperationExchange(ptName, opName, exchange)
									if(element) pre('class':"prettyprint", "${element.requestTemplate}" )
									else pre('N/A')
								}
								td(width:"50%"){
									Element element = doc2.getElementforOperationExchange(ptName, opName, exchange)
									if(element) pre('class':"prettyprint", "${element.requestTemplate}" )
									else pre('N/A')
								}
							}
						}
					}
					h2 "Schema Definition"
					p '''The following schemas are only showing definitions that are
									relevant to the message. The real schemas might be bigger.'''
					table('class' : "table table-striped table-bordered")  {
						thead{
							tr{
								th('Original')
								th('Modified')
							}
						}
						tbody{
							tr{
								td(width:"50%"){
									Element element = doc1.getElementforOperationExchange(ptName, opName, exchange)
									if(element){
										pre('class':"prettyprint", new SchemaSubsetVisitor().getSchemaAsString(element) )
									}
									else pre('N/A')
								}
								td(width:"50%"){
									Element element = doc2.getElementforOperationExchange(ptName, opName, exchange)
									if(element) {
										pre('class':"prettyprint", new SchemaSubsetVisitor().getSchemaAsString(element))
									}
									else pre('N/A')
								}
							}
						}
					}
				}
			}
		}
		return
	}

	private dumpOperationDiff(Difference diff) {
		builder.ul (id:"diffs" , 'class':"treeview") {
			li {
				div (diff.description) {
					if(!diff.diffs && diff.breaks()) img( src: "../web/images/lightning.png",  title: "This change will invalidate the interface")
					if(!diff.diffs && diff.safe()) img( src: "../web/images/tick.png",  title: "This change will not influence the interface")
				}
				diff.diffs.each { dumpOperationDiff(it) }
			}
		}
	}

	public String getCliUsage() {
		'wsdldiff <first-wsdl> <second-wsdl> [report directory]'
	}

	public getParser() {
		new WSDLParser()
	}

	public getStylesheet(format) {
		"${System.getenv('SOA_MODEL_HOME')}/src/main/style/wsdl2"+format+".xsl"
	}

	public getDiffGenerator(doc1, doc2) {
		new WsdlDiffGenerator(doc1, doc2)
	}
}
