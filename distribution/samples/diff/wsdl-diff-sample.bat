@echo off

pushd ..\..

bin\wsdldiff samples\diff\original\article.wsdl samples\diff\modified\article.wsdl samples\diff\wsdl-diff-output

pushd samples\diff

:end