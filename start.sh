#!/bin/bash

# Start the main app
java -jar /services/app.jar &

# Start the consumer app
java -jar /services/consumer.jar &

# Wait for background processes
wait
