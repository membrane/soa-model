#!/bin/sh
# find out folder that the script is stored in (so it can be called from anywhere)
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
cd $DIR/../..
sh bin/schemadiff.sh samples/diff/original/article.xsd samples/diff/modified/article.xsd samples/diff/schema-diff-output
cd $DIR
