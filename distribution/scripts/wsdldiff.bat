@echo off
if not "%SOA_MODEL_HOME%" == "" goto homeSet
set "SOA_MODEL_HOME=%cd%"
if exist "%SOA_MODEL_HOME%\bin\wsdldiff.bat" goto homeOk

:homeSet
if exist "%SOA_MODEL_HOME%\bin\wsdldiff.bat" goto homeOk
echo Please set the SOA_MODEL_HOME environment variable to point to
echo the directory where you have extracted the SOA Model software 
echo and put it in your path environment variable.
goto end

:homeOk
java  -classpath "%SOA_MODEL_HOME%/lib/*" org.membrane_soa.soa_model.diff.WSDLDiffCLI %1 %2 %3 %4 %5 %6

:end
