@echo off
cd /d %~dp0\..
set basedir=%CD%
set SERVICE_NAME=traceServer-v3-xiaomi
set SRV=%BASEDIR%\bin\prunsrv.exe
set TRAC_NAME=TSUSER
set /p SERVICE_PID=<%BASEDIR%\log\server.pid

if not "%SERVICE_PID%"=="" (goto killProcess) else (goto checkService)

:killProcess
call taskkill /PID %SERVICE_PID% /F >nul
echo "killProcess:%errorlevel%"
type nul > %BASEDIR%\log\server.pid
goto checkService

:checkService
sc query %SERVICE_NAME% > NUL
if ERRORLEVEL 1060 (goto end) else (goto delService)

:delService
rem net user %TRAC_NAME% /delete >nul
rem echo "delUser:%errorlevel%"
echo Delete service
call "%SRV%" //DS//%SERVICE_NAME%
echo Delete service done
goto end

:end
echo %SERVICE_NAME% is Deleted!