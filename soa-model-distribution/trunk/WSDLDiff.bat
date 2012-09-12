@echo off
set "CLASSPATH=%CLASSPATH%;./lib/soa-model-core-1.2.0.jar"
set "CLASSPATH=%CLASSPATH%;./lib/commons-codec-1.3.jar"
set "CLASSPATH=%CLASSPATH%;./lib/commons-httpclient-3.1.jar"
set "CLASSPATH=%CLASSPATH%;./lib/commons-logging-1.1.1.jar"
set "CLASSPATH=%CLASSPATH%;./lib/groovy-all-1.8.6.jar"
set "CLASSPATH=%CLASSPATH%;./lib/commons-cli-1.2.jar"
set "CLASSPATH=%CLASSPATH%;./bin"
echo SOA Model Diff Generator running...
java  -classpath "%CLASSPATH%" diff.groovy.WSDLDiffCLI %1 %2 %3 %4 %5 %6

:end