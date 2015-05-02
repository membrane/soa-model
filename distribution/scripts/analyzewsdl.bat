@echo off
if not "%SOA_MODEL_HOME%" == "" goto homeSet
set "SOA_MODEL_HOME=%cd%"
if exist "%SOA_MODEL_HOME%\bin\analysewsdl.bat" goto homeOk

:homeSet
if exist "%SOA_MODEL_HOME%\bin\analyzewsdl.bat" goto homeOk
echo Please set the SOA_MODEL_HOME environment variable to point to
echo the directory where you have extracted the SOA Model software 
echo and put it in your path environment variable.
goto end

:homeOk
set "SOA_CP=%CLASSPATH%"
set "SOA_CP=%SOA_CP%;%SOA_MODEL_HOME%/lib/soa-model-distribution-${version}.jar"
set "SOA_CP=%SOA_CP%;%SOA_MODEL_HOME%/lib/soa-model-core-${version}.jar"
set "SOA_CP=%SOA_CP%;%SOA_MODEL_HOME%/lib/commons-codec-1.6.jar"
set "SOA_CP=%SOA_CP%;%SOA_MODEL_HOME%/lib/httpclient-4.2.2.jar"
set "SOA_CP=%SOA_CP%;%SOA_MODEL_HOME%/lib/httpcore-4.2.2.jar"
set "SOA_CP=%SOA_CP%;%SOA_MODEL_HOME%/lib/commons-logging-1.1.1.jar"
set "SOA_CP=%SOA_CP%;%SOA_MODEL_HOME%/lib/groovy-2.3.9.jar"
set "SOA_CP=%SOA_CP%;%SOA_MODEL_HOME%/lib/groovy-xml-2.3.9.jar"
set "SOA_CP=%SOA_CP%;%SOA_MODEL_HOME%/lib/asm-4.0.jar"
set "SOA_CP=%SOA_CP%;%SOA_MODEL_HOME%/lib/commons-cli-1.2.jar"
set "SOA_CP=%SOA_CP%;%SOA_MODEL_HOME%/lib/slf4j-api-1.7.9.jar"
java  -classpath "%SOA_CP%" org.membrane_soa.soa_model.analyzer.WSDLAnalyzer %1 %2 %3 %4 %5 %6

:end