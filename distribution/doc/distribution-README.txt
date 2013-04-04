Membrane SOA Model 
==================

Command Line Tools
------------------
WSDL Diff:

bin\wsdldiff <<first wsdl file or url>> <<second wsdl file or url>> [<<report output folder>>]

Example:
1. cd c:\soa-model-home
2. c:\soa-model-home>bin\wsdldiff samples\diff\1\article.wsdl samples\diff\2\article.wsdl



Schema Diff:

bin\schemadiff <<first schema file or url>> <<second schema file or url>> [<<report output folder>>]

Example:
1. cd c:\soa-model-home
2. c:\soa-model-home>bin\schemadiff samples\diff\1\common.xsd samples\diff\2\common.xsd



If no output folder is given, the report will be generated in 'diff-report' in your current directory.




Running the Samples in Eclipse
------------------------------
The binary distribution of SOA Model can be imported as a project to eclipse by the following steps:

1. In eclipse click on File/New/Java Project...
2. In the Wizard uncheck 'Use default location' and click 'Browse...'
3. Choose the directory, where you've extracted the soa-model-distribution-x-bin.zip
4. Click on Finish
5. Run any of the programs in the samples package