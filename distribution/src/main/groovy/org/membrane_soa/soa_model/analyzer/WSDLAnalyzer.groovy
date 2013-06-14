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

package org.membrane_soa.soa_model.analyzer

import groovy.xml.MarkupBuilder

import com.predic8.wsdl.Definitions
import com.predic8.wsdl.WSDLParser

class WSDLAnalyzer{

	String url
	String reportFolder = 'analyzewsdl-report'
	Definitions wsdl
	def builder

	public static void main(String[] args) {
		WSDLAnalyzer wa = new WSDLAnalyzer().start(args)
	}

	public start(args){
		setUp(args)
		dump()
	}

	public void setUp(args){
		def cli = new CliBuilder()
		cli.usage = getCliUsage()

		def options = cli.parse(args)

		if(options.getArgs().size() >= 1){
			url= options.getArgs()[0]
			def parser = new WSDLParser()
			try {
				wsdl = parser.parse(url)
			} catch (IOException e) {
				println "Can not parse the document from: ${url}"
				cli.usage()
				System.exit(1)
			}
			if(options.getArgs().size() > 1) reportFolder = options.getArgs()[1]
		}
		else {
			cli.usage()
			System.exit(1)
		}
	}

	public String getCliUsage() {
		'analysewsdl <wsdl-document> [output directory]'
	}

	void dump(){
		new File(reportFolder).mkdir()
		new File("$reportFolder/web").mkdir()
		copy("${System.getenv('SOA_MODEL_HOME')}/src/web","$reportFolder/web")
		def writer = new FileWriter(new File("$reportFolder/index.htm"))
		builder = new MarkupBuilder(writer)
		builder.html(){
			head {
				title "WSDL Analyser: ${wsdl.targetNamespace}"
				link (rel : 'stylesheet' , href : "web/a.css")
			}
			body{
				h1('WSDL Analyser Result')
				h2("Analysed the WSDL:")
				div{
					table('class':'s1', 'border':'1'){
						thead{
							tr{
								th("URL")
								th("TargetNamespace")
//								th("Style")
							}
						}
						tbody{
							tr{
								td{span(url)}
								td{span(wsdl.targetNamespace)}
//								td{span(wsdl.style)}
							}
						}
					}

					table('class':'s1', 'border':'1'){
						thead{
							tr{
								th("Service")
								th("Ports")
								th("Locations")
							}
						}
						tbody{
							wsdl.services.each {service ->
								td{span(service.name)}
								td{
									service.ports.each{ port ->
										span(port.name)
										br()
									}
								}
								td{
									service.ports.each{ port ->
										span(port.address.location)
										br()
									}
								}
							}
						}
					}

					h3('PortTypes:')
					wsdl.portTypes.each {pt ->
						table('class':'s1', 'border':'1'){
							thead{
								tr{
									th("Name")
									th("Operations")
								}
							}
							tbody{
								tr{
									td{span(pt.name)}
									td{
										pt.operations.each {
											span(it.name)
											br()
										}
									}
								}
							}
						}
					}

					h3('Operations:')
					table('class':'s1', 'border':'1'){
						thead{
							tr{
								th("Name")
								th("Input Message")
								th("Output Message")
							}
						}
						tbody{
							wsdl.operations.each {op ->
								tr{
									td{span(op.name)}
									td{span(op.input.message.parts[0].element)}
									td{span(op.output.message.parts[0].element)}
								}
							}
						}
					}
				}
			}
		}
	}

	def copy(from, to) {
		new File(from).listFiles().each { entry ->
			if(entry.name.contains('svn')) return
				if (entry.isDirectory()) {
					new File("$to/${entry.name}").mkdir()
					copy("$from/${entry.name}", "$to/${entry.name}")
					return
				}
			new File("$to/${entry.name}").newOutputStream() << new File("$from/${entry.name}").newDataInputStream()
		}
	}
}
