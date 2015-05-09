#!/bin/sh
# find out folder that the script is stored in (so it can be called from anywhere)
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
cd $DIR/../..
sh bin/wsdldiff.sh samples/diff/original/article.wsdl samples/diff/modified/article.wsdl samples/diff/wsdl-diff-output
cd $DIR
