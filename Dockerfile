# Start with Eclipse Temurin 17 as the base image
FROM eclipse-temurin:17-jdk AS javabuilder

FROM node:22.12.0-bullseye
RUN apt-get update && apt-get install -y procps curl nano dos2unix \
    && curl -fsSL https://deb.nodesource.com/setup_22.x | bash - \
    && apt-get install -y nodejs \
    && node --version \
    && npm --version

# Copy Java from the builder stage
COPY --from=javabuilder /opt/java/openjdk /opt/java/openjdk

# Set environment variables
ENV NODE_VERSION=22.12.0
ENV CRONICLE_VERSION=0.9.61
ENV JAVA_HOME=/opt/java/openjdk
ENV CRONICLE_foreground=0
ENV CRONICLE_echo=1
ENV CRONICLE_color=1
ENV debug_level=1
ENV HOSTNAME=main

# Install Cronicle
RUN mkdir -p /opt/cronicle
RUN mkdir -p /opt/cronicle/plugins/java/lib
WORKDIR /opt/cronicle
RUN curl https://raw.githubusercontent.com/jhuckaby/Cronicle/master/bin/install.js | node

RUN ln -s /opt/java/openjdk/bin/java /opt/cronicle/bin
# Expose the application port
EXPOSE 3012

# Copy the startup script into the container
COPY external/entrypoint.sh /entrypoint.sh
# Copy the java-plugin into the container
COPY external/java-plugin.js /opt/cronicle/bin/java-plugin.js
# Copy the test jar into the container
COPY external/sample_method-1.0.jar /opt/cronicle/bin/sample_method.jar
# Copy the Rhino test jar into the container
COPY external/SampleTestRhino-1.0.jar /opt/cronicle/bin/SampleTestRhino.jar


RUN chmod +x /entrypoint.sh

# Use the startup script as the entrypoint
ENTRYPOINT ["/entrypoint.sh"]