@echo off
rem usage:
rem   build clean install site
rem   build assembly:assembly
mvn -Dmaven.test.skip=true %1 %2 %3 %4