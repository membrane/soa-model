#!/bin/sh
SCRIPTNAME=`basename $0`
if [ ! -e "${SOA_MODEL_HOME}/bin/${SCRIPTNAME}" ]; then # if script can't find itself...
	SOA_MODEL_HOME=$PWD # ... try pwd
	if [ ! -e "${SOA_MODEL_HOME}/bin/${SCRIPTNAME}" ]; then # if script can't find itself...
		SOA_MODEL_HOME="$PWD/.." # ... try pwd/..
		if [ ! -e "${SOA_MODEL_HOME}/bin/${SCRIPTNAME}" ]; then # if script can't find itself...
			echo "Please set the SOA_MODEL_HOME environment variable to point to the"
			echo "directory where you have extracted the SOA Model distribution package"
			echo "and put it in your path environment variable."
			exit 1
		fi
	fi
fi # else: script can find itself, all good

export SOA_MODEL_HOME

java -classpath "${SOA_MODEL_HOME}/lib/*" \
  org.membrane_soa.soa_model.diff.WSDLDiffCLI \
  $1 $2 $3 $4 $5 $6

