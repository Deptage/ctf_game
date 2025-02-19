:: Change to the directory where the batch file is located
cd /d %~dp0

:: Build headless browser image
cd headless_browser
docker build -t headless .
cd ..
:: build home_screen image
docker compose -f .\docker-compose-all.yml build

:: build aqua_messenger image
docker compose -f ..\aqua_messenger\docker-compose-all.yml build

:: build forum image
docker compose -f ..\forum\docker-compose-all.yml build

:: build bank image
docker compose -f ..\bank_page\docker-compose-all.yml build

:: build company image
docker compose -f ..\company_page\docker-compose-all.yml build

:: Prevent the command prompt from closing immediately
pause
