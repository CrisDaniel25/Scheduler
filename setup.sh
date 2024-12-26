#!/bin/bash

echo "Setting up Cronicle with persistent storage..."

# Create volume
docker volume create cronicle-data

# Run Cronicle container
docker run -d \
  --name cronicle \
  -p 3012:3012 \
  -v cronicle-data:/opt/cronicle/data \
  -e "CRONICLE_base_app_url=http://localhost:3012" \
  cronicle/cronicle

# Wait for Cronicle to initialize
echo "Waiting for Cronicle to start..."
sleep 10

echo "Deploying HelloWorld job..."

# Compile and copy HelloWorld.jar
javac HelloWorld.java
jar -cvf HelloWorld.jar HelloWorld.class
docker cp HelloWorld.jar cronicle:/opt/cronicle/jobs/HelloWorld.jar

echo "Setting up JavaRunner plugin..."

# Example: Automate plugin and job creation using Cronicle API
curl -X POST "http://localhost:3012/api/create_plugin" \
  -H "Content-Type: application/json" \
  -u admin:admin \
  -d '{
    "id": "java_runner",
    "title": "Java Runner",
    "executable_path": "java",
    "command_template": "java -jar /opt/cronicle/jobs/<%= args.jarName %
