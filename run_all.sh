docker build -t headless ./home_screen/headless_browser

caddy run --config ./caddy/Caddyfile &

docker-compose up --build

