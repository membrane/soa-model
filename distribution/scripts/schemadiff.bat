@echo off
if not "%SOA_MODEL_HOME%" == "" goto homeSet
set "SOA_MODEL_HOME=%cd%"
if exist "%SOA_MODEL_HOME%\bin\schemadiff.bat" goto homeOk

:homeSet
if exist "%SOA_MODEL_HOME%\bin\schemadiff.bat" goto homeOk
echo Please set the SOA_MODEL_HOME environment variable to point to
echo the directory where you have extracted the SOA Model software 
echo and put it in your path environment variable.
goto end

:homeOk
set "CLASSPATH=%CLASSPATH%;%SOA_MODEL_HOME%/lib/soa-model-distribution-${version}.jar"
set "CLASSPATH=%CLASSPATH%;%SOA_MODEL_HOME%/lib/soa-model-core-${version}.jar"
set "CLASSPATH=%CLASSPATH%;%SOA_MODEL_HOME%/lib/commons-codec-1.6.jar"
set "CLASSPATH=%CLASSPATH%;%SOA_MODEL_HOME%/lib/httpclient-4.2.2.jar"
set "CLASSPATH=%CLASSPATH%;%SOA_MODEL_HOME%/lib/httpcore-4.2.2.jar"
set "CLASSPATH=%CLASSPATH%;%SOA_MODEL_HOME%/lib/commons-logging-1.1.1.jar"
set "CLASSPATH=%CLASSPATH%;%SOA_MODEL_HOME%/lib/groovy-2.0.4.jar"
set "CLASSPATH=%CLASSPATH%;%SOA_MODEL_HOME%/lib/groovy-xml-2.0.4.jar"
set "CLASSPATH=%CLASSPATH%;%SOA_MODEL_HOME%/lib/asm-4.0.jar"
set "CLASSPATH=%CLASSPATH%;%SOA_MODEL_HOME%/lib/commons-cli-1.2.jar"
java  -classpath "%CLASSPATH%" org.membrane_soa.soa_model.diff.SchemaDiffCLI %1 %2 %3 %4 %5 %6

:end