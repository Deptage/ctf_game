# # Stage 1: Build the React app
FROM node:23.1.0-alpine3.19 AS build

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies using Yarn
RUN yarn install --verbose

# Copy the rest of the application code
COPY src ./src
COPY public ./public

# Build the React app using Yarn
RUN yarn build

# Stage 2: Serve the app with Nginx
FROM nginx:alpine

RUN apk update && apk add curl
# Copy the build output to Nginx html directory
COPY --from=build /app/build /usr/share/nginx/html
RUN        chown -R nginx:nginx /var/cache/nginx && \
        chown -R nginx:nginx /var/log/nginx && \
        chown -R nginx:nginx /etc/nginx/conf.d

COPY nginx.conf /etc/nginx/conf.d/default.conf

RUN touch /var/run/nginx.pid && \
        chown -R nginx:nginx /var/run/nginx.pid
USER nginx
# Expose port 8080
EXPOSE 8080

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]