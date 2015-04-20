#!/bin/sh
SCRIPTNAME=`basename $0`
if [ -n $SOA_MODEL_HOME ]; then # check if SOA_MODEL_HOME is set
	if [ ! -e "${SOA_MODEL_HOME}/bin/${SCRIPTNAME}" ]; then # if script can't find itself...
		echo "Invalid SOA_MODEL_HOME directory specified"
		exit 1
	fi
else # if SOA_MODEL_HOME is not set...
	SOA_MODEL_HOME=$PWD # ... try pwd
	if [ ! -e "${SOA_MODEL_HOME}/bin/${SCRIPTNAME}" ]; then # if script can't find itself...
		echo "Please set the SOA_MODEL_HOME environment variable to point to the"
		echo "directory where you have extracted the SOA Model distribution package"
		echo "and put it in your path environment variable."
		exit 1
	fi
fi

CLASSPATH="${CLASSPATH}:${SOA_MODEL_HOME}/lib/soa-model-distribution-${version}.jar"
CLASSPATH="${CLASSPATH}:${SOA_MODEL_HOME}/lib/soa-model-core-${version}.jar"
CLASSPATH="${CLASSPATH}:${SOA_MODEL_HOME}/lib/commons-codec-1.6.jar"
CLASSPATH="${CLASSPATH}:${SOA_MODEL_HOME}/lib/httpclient-4.2.2.jar"
CLASSPATH="${CLASSPATH}:${SOA_MODEL_HOME}/lib/httpcore-4.2.2.jar"
CLASSPATH="${CLASSPATH}:${SOA_MODEL_HOME}/lib/commons-logging-1.1.1.jar"
CLASSPATH="${CLASSPATH}:${SOA_MODEL_HOME}/lib/groovy-2.0.4.jar"
CLASSPATH="${CLASSPATH}:${SOA_MODEL_HOME}/lib/groovy-xml-2.0.4.jar"
CLASSPATH="${CLASSPATH}:${SOA_MODEL_HOME}/lib/asm-4.0.jar"
CLASSPATH="${CLASSPATH}:${SOA_MODEL_HOME}/lib/commons-cli-1.2.jar"
java -classpath "${CLASSPATH}" \
  org.membrane_soa.soa_model.analyzer.WSDLAnalyzer \
  $1 $2 $3 $4 $5 $6
