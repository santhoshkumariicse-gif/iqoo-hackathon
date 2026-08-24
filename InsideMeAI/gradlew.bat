@rem ##########################################################################
@rem  Gradle startup script for Windows
@rem ##########################################################################
@if "%DEBUG%"=="" @echo off
@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal
set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem Set JAVA_HOME to Android Studio JBR if not already set
if "%JAVA_HOME%"=="" set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
set GRADLE_USER_HOME=%USERPROFILE%\.gradle
set "WRAPPER_JAR=%DIRNAME%gradle\wrapper\gradle-wrapper.jar"
set "DIST_PATH=%GRADLE_USER_HOME%\wrapper\dists\gradle-8.14-all\c2qonpi39x1mddn7hk5gh9iqj\gradle-8.14"
set "GRADLE_EXEC=%DIST_PATH%\bin\gradle.bat"
if not exist "%GRADLE_EXEC%" (
  echo Gradle not found at %GRADLE_EXEC%
  exit /b 1
)
set "APP_BASE_NAME=%~n0"
"%GRADLE_EXEC%" "-Dorg.gradle.appname=%APP_BASE_NAME%" %*
