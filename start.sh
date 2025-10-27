#!/bin/bash

# start.sh
# This script launches two services in the background and waits for either to fail.

# Check if PORT is set by the environment, default to 8080 if not (as defined in Dockerfile)
PORT=${PORT:-8080}
echo "--- Starting Java Services (JDK 21) ---"
echo "Listening port for app.jar is $PORT"

# The 'app' service is assumed to be the web server, so it must listen on $PORT.
echo "Starting App Service (java -Dserver.port=$PORT -jar app.jar)..."
java -Dserver.port=$PORT -jar app.jar &
APP_PID=$!
echo "App Service PID: $APP_PID"

# The 'consumer' service typically does not need a port, so it runs normally.
echo "Starting Consumer Service (java -jar consumer.jar)..."
java -jar consumer.jar &
CONSUMER_PID=$!
echo "Consumer Service PID: $CONSUMER_PID"

echo "--- Services Running in Background ---"
echo "Container will exit if either the App (PID: $APP_PID) or Consumer (PID: $CONSUMER_PID) fails."


# Wait for one of the processes to exit (-n)
wait -n $APP_PID $CONSUMER_PID

# Get the exit code of the process that caused the wait to finish
EXIT_CODE=$?
echo "One of the services terminated with exit code $EXIT_CODE. Container shutting down."

# Exit the container with the failing service's exit code
exit $EXIT_CODE
