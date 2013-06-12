Membrane SOA Model 
==================

Command Line Tools
------------------
WSDL Diff:

bin\wsdldiff <<first wsdl file or url>> <<second wsdl file or url>> [<<report output folder>>]

Example:
1. cd c:\soa-model-home
2. c:\soa-model-home>bin\wsdldiff samples\diff\original\article.wsdl samples\diff\modified\article.wsdl



Schema Diff:

bin\schemadiff <<first schema file or url>> <<second schema file or url>> [<<report output folder>>]

Example:
1. cd c:\soa-model-home
2. c:\soa-model-home>bin\schemadiff samples\diff\original\common.xsd samples\diff\modified\common.xsd



If no output folder is given, the report will be generated in 'diff-report' in your current directory.





Diff Examples
-------------
To generate a sample diff report for WSDL:

1. go to 'samples\diff\' folder
2. run the 'wsdl-sample-diff.bat' with a double click
3. go to 'wsdl-diff-output' and open 'diff-report.html'



To generate a sample diff report for XSD Schema:

1. go to 'samples\diff\' folder
2. run the 'schema-sample-diff.bat' with a doble click
3. go to 'schema-diff-output' and open 'diff-report.html'





Running the Samples in Eclipse
------------------------------
The binary distribution of SOA Model can be imported as a project to eclipse by the following steps:

1. In eclipse click on File/Import...
2. In the Wizard select 'General' --> 'Existing Projects into Workspace' as import source and click 'Next'
3. Select root directory and Click on 'Browse...' 
4. Choose the directory, where you've extracted the soa-model-distribution-x-bin.zip
5. Click on Finish
6. Run any of the programs in the samples package
