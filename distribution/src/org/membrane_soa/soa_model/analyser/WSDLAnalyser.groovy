package org.membrane_soa.soa_model.analyser

import groovy.xml.MarkupBuilder

import com.predic8.wsdl.Definitions
import com.predic8.wsdl.WSDLParser

class WSDLAnalyser{

	String url
	String reportFolder = 'analysewsdl-report'
	Definitions wsdl
	def builder

	public static void main(String[] args) {
		WSDLAnalyser wa = new WSDLAnalyser().start(args)
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
		'analysewsdl <wsdl-document>'
	}

	void dump(){
		new File(reportFolder).mkdir()
		new File("$reportFolder/static").mkdir()
		copy("${System.getenv('SOA_MODEL_HOME')}/static","$reportFolder/static")
		def writer = new FileWriter(new File("$reportFolder/index.htm"))
		builder = new MarkupBuilder(writer)
		builder.html(){
			head {
				title "WSDL Analyser: ${wsdl.targetNamespace}"
				link (rel : 'stylesheet' , href : "static/a.css")
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
								th("Style")
							}
						}
						tbody{
							tr{
								td{span(url)}
								td{span(wsdl.targetNamespace)}
								td{span(wsdl.style)}
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
