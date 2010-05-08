#!/bin/bash
mvn install:install-file -DgroupId=org.swinglabs -DartifactId=swingx -Dversion=1.6 -Dpackaging=jar -Dfile=swingx-1.6-sources.jar -Dclassifier=sources -DgeneratePom=false
