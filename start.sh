#!/bin/bash

# Move to the services directory
cd /services || exit 1

# Start the main app in background
java -jar app.jar &

# Start the consumer app in background
java -jar consumer.jar &

# Keep the container alive
wait
