#!/bin/bash

# Start the main app
cd app
java -jar target/*.jar &

# Start the consumer app
cd ../consumer
java -jar target/*.jar &
