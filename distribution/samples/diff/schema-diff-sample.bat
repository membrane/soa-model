@echo off

pushd ..\..

bin\schemadiff samples\diff\original\article.xsd samples\diff\modified\article.xsd samples\diff\schema-diff-output

pushd samples\diff

:end