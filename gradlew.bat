@echo off
@rem
@rem Copyright 2015-2021 the original authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

set DEFAULT_JVM_OPTS="-Xmx64m" "-Xms64m"

set CLASSPATH=%APP_HOME%gradle\wrapper\gradle-wrapper.jar

@rem Determine the Java command to use to start the JVM.
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% == 0 goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
 echo location of your Java installation.
echo.
goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%\bin\java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
 echo location of your Java installation.
echo.
goto fail

:execute
@rem Setup the command line
set CMD_LINE_ARGS=

:loop
if "%1"=="" goto endLoop
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto loop

:endLoop

%JAVA_EXE% %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %CMD_LINE_ARGS%

if %ERRORLEVEL% == 0 goto end

:fail
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% == 0 set EXIT_CODE=1
if not ""=="%CMD_LINE_ARGS%" echo.
echo EXIT_CODE=%EXIT_CODE%

:end
if ""=="%CMD_LINE_ARGS%" echo.
endlocal

:omega
