:: Change to the directory where the batch file is located
cd /d %~dp0

:: Run npm start to start the application
npm start

:: Prevent the command prompt from closing immediately
pause
