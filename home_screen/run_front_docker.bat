:: Change to the directory where the batch file is located
cd /d %~dp0

:: Run Docker Compose to start the application
docker compose -f .\docker-for-frontend-devs.yml up

:: Prevent the command prompt from closing immediately
pause
