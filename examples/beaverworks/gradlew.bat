@echo off
set "JAVA=java"
if defined JAVA_HOME set "JAVA=%JAVA_HOME%\bin\java.exe"
"%JAVA%" -classpath "%~dp0gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
