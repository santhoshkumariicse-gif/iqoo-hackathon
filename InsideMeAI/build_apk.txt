@echo off
setlocal
set "PATH=C:\Program Files\Android\Android Studio\jbr\bin;%PATH%"
set "ANDROID_HOME=C:\Users\santh\AppData\Local\Android\Sdk"
set "GRADLE_BIN=C:\Users\santh\.gradle\wrapper\dists\gradle-8.14-all\c2qonpi39x1mddn7hk5gh9iqj\gradle-8.14\bin\gradle.bat"
cd /d "d:\hackathon\iqoo\InsideMeAI"
call "%GRADLE_BIN%" assembleDebug --debug 2>&1 > build_debug.txt
findstr /c:"Exception" /c:"Could not" /c:"25.0" /c:"Reason" /c:"NDK" /c:"buildTools" /c:"Caused by" /c:"Failed to" build_debug.txt
