Membrane SOA Model 
==================
Check the [Repository at GitHub](https://github.com/membrane/soa-model) for the latest source code.
What is SOA Model?
-
Membrane SOA Model is an open-source lightweight Java API for WSDL and XML Schema, licensed under ASF 2.0. that can be used to:

- Parse, create or modify a WSDL or XML Schema Document
- Compare two WSDL Documents
- Compare two XML Schema Documents
- Create a SOAP Request
- Create a SOAP Request Template
- Analyze a WSDL or Schema document and generate an HMTL report

Command Line Tools
------------------
SOA Model provides helpful command line tools beside the java api.
You are able to:

- Analyze a WSDL Document and Generate a Report
- Create a SOAP Request Template for a WSDL
- Compare two WSDL or Schema Documents

All you need is to download and run the batch with the given documents.

Code Samples
-----------
Here are some examples of the tasks you can achive with SOA Model.
For more details see the [documentation](http://membrane-soa.org/soa-model-doc/).

Parsing a wsdl:

    WSDLParser parser = new WSDLParser();
    Definitions defs = parser.parse("http://ws.xwebservices.com/XWebBlog/V2/XWebBlog.wsdl");

Creating a new wsdl:

    Definitions wsdl = new Definitions("http://predic8.com/wsdl/AddService/1/", "AddService");
    wsdl.add(schema);
    PortType pt = wsdl.newPortType("AddPortType");
    Operation op = pt.newOperation("add");
    op.newInput("add").newMessage("add").newPart("parameters", "tns:add");
    op.newOutput("addResponse").newMessage("addResponse").newPart("parameters", "tns:addResponse");
    return wsdl;
Comparing two wsdls:

    Definitions wsdl1 = parser.parse("resources/diff/1/article.wsdl");
    Definitions wsdl2 = parser.parse("resources/diff/2/article.wsdl");
    WsdlDiffGenerator diffGen = new WsdlDiffGenerator(wsdl1, wsdl2);
    List<Difference> lst = diffGen.compare();
Comparing two schemas:

    Schema schema1 = parser.parse("resources/diff/1/common.xsd"); 
    Schema schema2 = parser.parse("resources/diff/2/common.xsd");
    SchemaDiffGenerator diffGen = new SchemaDiffGenerator(schema1, schema2);
    List<Difference> lst = diffGen.compare();
Creating a soap request:

    Definitions wsdl = parser.parse("resources/article/article.wsdl");
    StringWriter writer = new StringWriter();
    SOARequestCreator creator = new SOARequestCreator(wsdl, new RequestTemplateCreator(), new MarkupBuilder(writer));
    //creator.createRequest(PortType name, Operation name, Binding name);
    creator.createRequest("ArticleServicePT", "create", "ArticleServicePTBinding");
    System.out.println(writer);