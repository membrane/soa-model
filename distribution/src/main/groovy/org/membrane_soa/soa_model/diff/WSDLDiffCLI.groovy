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
		diffCLI.createOperationPages()
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
			OperationChanges{
				def ops = (doc1.operations.name + doc2.operations.name).unique().sort()
				ops.each { opName ->

					Operation('name':opName){

						Difference change = findOperationChanges(diffs, opName)
						if(change) {
							if(change.description.contains("Operation $opName added.")){
								added()
								createOperation(builder, doc2, opName, change)
							}
							else if(change.description.contains("Operation $opName removed.")){
								removed()
								createOperation(builder, doc1, opName, change)
							}
							else if(change.description.contains("Operation $opName:")){
//								if(change.description.contains("Operation $opName has changed")){
								createOperation(builder, doc1, opName, change)
							}
						}
					}
				}
			}
			Diffs{
				diffs.each{ diff -> dump(diff) }
			}
		}

		new File(reportFolder).mkdir()
		File xml = new File("$reportFolder/diff-report.xml")
		OutputStream outputStream = new FileOutputStream (xml);
		writer.writeTo(outputStream);
		transform(new ByteArrayInputStream(writer.toByteArray()), 'html')
	}
	
	private createOperation(MarkupBuilder builder, Definitions wsdl, String opName, Difference change) {
		Operation op = wsdl.operations.find{it.name == opName}
		def inputDiff = change.diffs.find{it.type == 'input'}
		builder.input(message: op.input?.message?.name , compatibility: computeCompatibility(inputDiff))
		def outputDiff = change.diffs.find{it.type == 'output'}
		builder.output(message: op.output?.message?.name , compatibility: computeCompatibility(outputDiff))
		//TODO Faults are missing!
	}
	
	private computeCompatibility(Difference diff){
		if(!diff) return ''
		diff.safe() ? 'safe' : (diff.breaks() ? 'breaking' : 'not clear')
	}
	
	private findOperationChanges(diffs, opName) {
		for(def diff in diffs) {
			if(diff.type?.contains("operation") && diff.description.contains("Operation $opName")){
				return diff
			}
		}
		if(diffs.diffs) findOperationChanges(diffs.diffs.flatten(), opName)
	}

	void createOperationPages() {
		new File("$reportFolder/operations").mkdir()
		def opNames = (doc1.operations.name + doc2.operations.name).unique().sort()
		opNames.each { opName ->
			generateOperationPages(opName, 'input')
			generateOperationPages(opName, 'output')
		}
	}
	
	private generateOperationPages(String opName, exchange) {
		def writer = new FileWriter(new File("$reportFolder/operations/$opName-${exchange}.html"))
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
					Difference opChange = findOperationChanges(diffs, opName)
					if(!opChange|| !opChange.diffs.type.contains(exchange)) h2 'There are no differences.'
					else {
						h2 "Differences:"
						div {
							opChange.diffs.findAll {it.type == exchange}.each { diff -> 
								dumpOperationDiff(diff)
							}
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
								td{
									Element element = doc1.getElementforOperationExchange(opName, exchange)
									if(element) pre('class':"prettyprint", "${element.requestTemplate}" )
								}
								td{
									Element element = doc2.getElementforOperationExchange(opName, exchange)
									if(element) pre('class':"prettyprint", "${element.requestTemplate}" )
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
								td{
									Element element = doc1.getElementforOperationExchange(opName, exchange)
									if(element){
										pre('class':"prettyprint", new SchemaSubsetVisitor().getSchemaAsString(element) )
									}
								}
								td{
									Element element = doc2.getElementforOperationExchange(opName, exchange)
									if(element) {
										pre('class':"prettyprint", new SchemaSubsetVisitor().getSchemaAsString(element))
									}
								}
							}
						}
					}
				}
			}
		}
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
