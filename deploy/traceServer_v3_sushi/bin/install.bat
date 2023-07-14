@echo off

set SERVICE_EN_NAME=traceServer-v3-xiaomi
set SERVICE_CH_NAME=traceServer-v3-xiaomi Demo
echo %SERVICE_CH_NAME%

cd /d %~dp0\..
set BASEDIR=%CD%
set CLASSPATH=%BASEDIR%\lib\server-xiaomi-V3-SNAPSHOT.jar
set MAIN_CLASS=org.springframework.boot.loader.JarLauncher

set JAVA_HOME=%BASEDIR%\jre
set SRV=%BASEDIR%\bin\prunsrv.exe

set LOGPATH=%BASEDIR%\log

echo BASEDIR: %BASEDIR%
echo SERVICE_NAME: %SERVICE_EN_NAME%
echo JAVA_HOME: %JAVA_HOME%
echo MAIN_CLASS: %MAIN_CLASS%
echo prunsrv path: %SRV%

if "%JVM%" == "" goto findJvm
if exist "%JVM%" goto foundJvm
:findJvm
set "JVM=%JAVA_HOME%\bin\server\jvm.dll"
if exist "%JVM%" goto foundJvm
echo can not find jvm.dll automatically,
echo please use COMMAND to localation it
echo then install service
goto end
:foundJvm
set TRACE_PWD=TSUSER123456
set TRAC_NAME=TSUSER
rem net user %TRAC_NAME% %TRACE_PWD% /add /passwordchg:no /passwordreq:no /times:ALL >nul
rem net localgroup administrators %TRAC_NAME% /add >nul
rem wmic useraccount where "Name='%TRAC_NAME%'" set PasswordExpires=False >nul
echo "userdone:%errorlevel%"
echo install service...
"%SRV%" install %SERVICE_EN_NAME% ^
--Description="%SERVICE_CH_NAME%" ^
--DisplayName="%SERVICE_CH_NAME%" ^
--Classpath="%CLASSPATH%" ^
--Install="%SRV%" ^
--JavaHome="%JAVA_HOME%" ^
--Jvm="%JVM%" ^
--JvmMs=256 ^
--JvmMx=2048 ^
--StartPath="%BASEDIR%" ^
--Startup=auto ^
--StartMode=jvm ^
--StartClass=%MAIN_CLASS% ^
--StartMethod=main ^
--StopMode=jvm ^
--StopClass=%MAIN_CLASS% ^
--StopMethod=stop ^
--LogPath=%LOGPATH% ^
--LogLevel=Trace ^
--StdOutput=auto ^
--StdError=auto ^
--PidFile=server.pid ^
--ServiceUser=LocalSystem
rem --ServiceUser="%computername%\%TRAC_NAME%" --ServicePassword=%TRACE_PWD% --PidFile=server.pid
echo Install finish and start service right now!
"%SRV%" start %SERVICE_EN_NAME%