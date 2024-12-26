#!/bin/bash

# Check if Cronicle has already been set up (this doesnt really work)
if [ ! -f "/opt/cronicle/data/config.json" ]; then
  echo "Cronicle setup not detected. Running setup..."
  ./bin/control.sh setup
else
  echo "Cronicle already set up. Skipping setup step."
fi

# Start Cronicle
./bin/control.sh start

# Keep the container running
tail -F /opt/cronicle/logs/Errors.log