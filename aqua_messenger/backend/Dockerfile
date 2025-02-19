# Etap 1: Budowanie projektu
FROM maven:3.8.1-openjdk-17-slim AS build
WORKDIR /app

# Skopiuj pliki źródłowe Maven do kontenera
COPY pom.xml .
COPY src ./src
COPY application-in-container.properties ./src/main/resources/application.properties

# Uruchom komendę Maven, aby zbudować projekt
RUN mvn clean install

# Etap 2: Uruchomienie aplikacji
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

USER root
# Zainstaluj curl
RUN apk update && apk add curl && apk add iptables su-exec


# Skopiuj zbudowany plik JAR z poprzedniego etapu
COPY --from=build /app/target/*.jar app.jar

# Utwórz nowego użytkownika i grupę
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Zmień właściciela plików na nowego użytkownika
RUN chown -R appuser:appgroup /app

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
