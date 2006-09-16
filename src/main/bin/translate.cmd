@echo off
rem HTMLParser Library - A java-based parser for HTML
rem http:remhtmlparser.org
rem Copyright (C) 2006 Derrick Oswald
rem
rem Revision Control Information
rem
rem $URL$
rem $Author$
rem $Date$
rem $Revision$
rem
rem This library is free software; you can redistribute it and/or
rem modify it under the terms of the Common Public License; either
rem version 1.0 of the License, or (at your option) any later version.
rem
rem This library is distributed in the hope that it will be useful,
rem but WITHOUT ANY WARRANTY; without even the implied warranty of
rem MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
rem Common Public License for more details.
rem
rem You should have received a copy of the Common Public License
rem along with this library; if not, the license is available from
rem the Open Source Initiative (OSI) website:
rem   http://opensource.org/licenses/cpl1.0.php
rem
setlocal enableextensions
if errorlevel 1 goto no_extensions_error
for %%i in ("%0") do set cmd_path=%%~dpi
for /D %%i in ("%cmd_path%..\lib\") do set lib_path=%%~dpi
if not exist "%lib_path%htmllexer.jar" goto no_htmllexer_jar_error
if not exist "%lib_path%htmlparser.jar" goto no_htmlparser_jar_error
for %%i in (java.exe) do set java_executable=%%~$PATH:i
if "%java_executable%"=="" goto no_java_error
@echo on
%java_executable% -classpath "%lib_path%htmlparser.jar;%lib_path%htmllexer.jar" org.htmlparser.util.Translate %1 %2
@echo off
goto end
:no_extensions_error
echo Unable to use CMD extensions
goto end
:no_htmllexer_jar_error
echo Unable to find htmllexer.jar
goto end
:no_htmlparser_jar_error
echo Unable to find htmlparser.jar
goto end
:no_java_error
echo Unable to find java.exe
goto end
:end
