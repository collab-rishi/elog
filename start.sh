#!/bin/bash



echo "--- Starting Java Services (JDK 21) ---"





echo "Starting App Service (java -jar app.jar)..."
java -jar app.jar &
APP_PID=$!
echo "App Service PID: $APP_PID"



The JAR was copied to /services/consumer.jar

echo "Starting Consumer Service (java -jar consumer.jar)..."
java -jar consumer.jar &
CONSUMER_PID=$!
echo "Consumer Service PID: $CONSUMER_PID"

echo "--- Services Running in Background ---"


wait -n $APP_PID $CONSUMER_PID



EXIT_CODE=$?
echo "One of the services terminated with exit code $EXIT_CODE. Container shutting down."
exit $EXIT_CODE