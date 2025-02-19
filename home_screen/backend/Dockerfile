# Stage 1: Build the project
FROM maven:3.8.1-openjdk-17-slim AS build
WORKDIR /app

# Copy Maven source files to the container
COPY pom.xml .
COPY src ./src
COPY application-in-container.properties ./src/main/resources/application.properties

# Run Maven command to build the project
RUN mvn clean install

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-alpine 
WORKDIR /app

COPY port-uuids.csv ./port-uuids.csv
# Install docker.io
RUN apk update && apk add docker docker-compose curl sudo

# Create a non-root user and group
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Allow the non-root user to run specific sudo commands without a password to be able to run docker compose commands, wildcards are for preventing user from running any other command
#RUN echo "appuser ALL=(ALL:ALL) NOPASSWD:SETENV: ALL" >> /etc/sudoers
RUN echo "Defaults env_keep += \"bPORT\"" >> /etc/sudoers
RUN echo "Defaults env_keep += \"INSTANCE_ID\"" >> /etc/sudoers
RUN echo "appuser ALL=(ALL) NOPASSWD: /usr/bin/docker compose -p [a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]* -f /aqua_messenger/docker-compose-all.yml up -d db backend csrf" >> /etc/sudoers
RUN echo "appuser ALL=(ALL) NOPASSWD: /usr/bin/docker compose -p [a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]* -f /bank_page/docker-compose-all.yml up -d db backend" >> /etc/sudoers
RUN echo "appuser ALL=(ALL) NOPASSWD: /usr/bin/docker compose -p [a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]* -f /forum/docker-compose-all.yml up -d db backend" >> /etc/sudoers
RUN echo "appuser ALL=(ALL) NOPASSWD: /usr/bin/docker compose -p [a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]* -f /company_page/docker-compose-all.yml up -d db backend" >> /etc/sudoers

RUN echo "appuser ALL=(ALL) NOPASSWD: /usr/bin/docker compose -p [a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]* down --rmi all --volumes" >> /etc/sudoers
RUN echo "appuser ALL=(ALL) NOPASSWD: /usr/bin/docker compose -p [a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]* down db backend --rmi all --volumes" >> /etc/sudoers


RUN echo "appuser ALL=(ALL) NOPASSWD: /usr/bin/docker compose -p [a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]* down --volumes" >> /etc/sudoers
RUN echo "appuser ALL=(ALL) NOPASSWD: /usr/bin/docker run --add-host=[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*.ctfgame.pl\:[0-9]*.[0-9]*.[0-9]*.[0-9]* --rm -e B64URL=http\://host.docker.internal\:1111/start/[a-zA-Z0-9]* --cap-add=SYS_ADMIN --network=[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*_messenger-network headless" >> /etc/sudoers
RUN echo "appuser ALL=(ALL) NOPASSWD: /usr/bin/docker run --add-host=[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*.ctfgame.pl\:[0-9]*.[0-9]*.[0-9]*.[0-9]* --rm -e B64URL=http\://host.docker.internal\:1111/start/[a-zA-Z0-9]*=* --cap-add=SYS_ADMIN --network=[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*_messenger-network headless" >> /etc/sudoers
RUN echo "appuser ALL=(ALL) NOPASSWD: /usr/bin/docker run --add-host=[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*.ctfgame.pl\:[0-9]*.[0-9]*.[0-9]*.[0-9]* --rm -e B64URL=http\://172.17.0.1\:1111/start/[a-zA-Z0-9]* --cap-add=SYS_ADMIN --network=[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*_messenger-network headless" >> /etc/sudoers
RUN echo "appuser ALL=(ALL) NOPASSWD: /usr/bin/docker run --add-host=[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*.ctfgame.pl\:[0-9]*.[0-9]*.[0-9]*.[0-9]* --rm -e B64URL=http\://172.17.0.1\:1111/start/[a-zA-Z0-9]*=* --cap-add=SYS_ADMIN --network=[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*_messenger-network headless" >> /etc/sudoers

RUN echo "appuser ALL=(ALL) NOPASSWD: /usr/bin/docker inspect -f {{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}} [a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-[a-zA-Z0-9]*-backend-1" >> /etc/sudoers

RUN chmod -R o+r /run
#sudo -u root /usr/bin/docker run --add-host=50d3553a-031b-4d86-bf61-674a8d221cf0.ctfgame.pl:172.21.0.3 --rm -e B64URL=http://host.docker.internal:1111/start/eyJpbnN0YW5jZV9pZCI6IjUwZDM1NTNhLTAzMWItNGQ4Ni1iZjYxLTY3NGE4ZDIyMWNmMCJ9 --cap-add=SYS_ADMIN --network=50d3553a-031b-4d86-bf61-674a8d221cf0_messenger-network headless
# Set the user to the non-root user
USER appuser 


# Copy the built JAR file from the previous stage
COPY --from=build /app/target/*.jar app.jar


# Set the entry point to run the Java application cat /run/secrets/db_password ls /run
ENTRYPOINT ["java", "-jar", "app.jar"]
