FROM python:3.9-alpine

RUN adduser -D user
WORKDIR /home/user

COPY server.py .
COPY entrypoint.sh /entrypoint.sh
COPY index.html .

RUN chmod +x /entrypoint.sh

ENTRYPOINT ["sh", "/entrypoint.sh"]
