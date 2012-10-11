@echo off
if not "%SOA_MODEL_HOME%" == "" goto homeSet
set "SOA_MODEL_HOME=%cd%"
if exist "%SOA_MODEL_HOME%\schemadiff.bat" goto homeOk

:homeSet
if exist "%SOA_MODEL_HOME%\schemadiff.bat" goto homeOk
echo Please set the SOA_MODEL_HOME environment variable to point to
echo the directory where you have extracted the SOA Model software.
goto end

:homeOk
set "CLASSPATH=%CLASSPATH%;%SOA_MODEL_HOME%/lib/soa-model-core-1.2.0.jar"
set "CLASSPATH=%CLASSPATH%;%SOA_MODEL_HOME%/lib/commons-codec-1.3.jar"
set "CLASSPATH=%CLASSPATH%;%SOA_MODEL_HOME%/lib/commons-httpclient-3.1.jar"
set "CLASSPATH=%CLASSPATH%;%SOA_MODEL_HOME%/lib/commons-logging-1.1.1.jar"
set "CLASSPATH=%CLASSPATH%;%SOA_MODEL_HOME%/lib/groovy-all-1.8.6.jar"
set "CLASSPATH=%CLASSPATH%;%SOA_MODEL_HOME%/lib/commons-cli-1.2.jar"
set "CLASSPATH=%CLASSPATH%;%SOA_MODEL_HOME%/bin"
java  -classpath "%CLASSPATH%" org.membrane_soa.soa_model.diff.SchemaDiffCLI %1 %2 %3 %4 %5 %6

:end