Membrane SOA Model 
==================

Command Line Tools
------------------


WSDL Diff:

bin\wsdldiff <FirstWsdlFileOrUrl> <SecondWsdlFileOrUrl> [<ReportOutputFolder>]

Windows Example:
1. change to SOA_MODEL_HOME folder
2. > bin\wsdldiff samples\diff\original\article.wsdl samples\diff\modified\article.wsdl

Linux/Mac Example:
1. change to SOA_MODEL_HOME folder
2. > bin/wsdldiff.sh samples/diff/{original,modified}/article.wsdl

If no output folder is given, the report will be generated in 'diff-report' in your current directory.


Schema Diff:

Windows: bin\schemadiff <FirstSchemaFileOrUrl> <SecondSchemaFileOrUrl> [<ReportOutputFolder>]
Linux: bin/schemadiff.sh <FirstSchemaFileOrUrl> <SecondSchemaFileOrUrl> [<ReportOutputFolder>]

Windows Example:
1. change to SOA_MODEL_HOME folder
2. > bin\schemadiff samples\diff\original\common.xsd samples\diff\modified\common.xsd

Linux/Mac Example:
1. change to SOA_MODEL_HOME folder
2. > bin/schemadiff.sh samples/diff/{original,modified}/common.xsd

If no output folder is given, the report will be generated in 'diff-report' in your current directory.



Running the Samples in Eclipse
------------------------------
The binary distribution of SOA Model can be imported as a project to eclipse by the following steps:

1. In eclipse click on File/Import...
2. In the Wizard select 'General' --> 'Existing Projects into Workspace' as
   import source and click 'Next'
3. Select root directory and Click on 'Browse...'
4. Choose the directory, where you've extracted the
   soa-model-distribution-VERSION-bin.zip
5. Click on Finish
6. Run any of the programs from src/main/java/samples/...

