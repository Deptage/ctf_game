# Use the zenika/alpine-chrome:with-puppeteer image as the base image
FROM zenika/alpine-chrome:with-puppeteer

# Set the working directory inside the container
WORKDIR /usr/src/app


# Copy the puppeteer.js script to the working directory
COPY puppeteer.js /usr/src/app/src/puppeteer.js



# Define the entry point to run the puppeteer.js script
ENTRYPOINT ["node", "src/puppeteer.js"]