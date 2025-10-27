# --- STAGE 1: BUILD THE 'APP' SERVICE ---
FROM maven:3.9-eclipse-temurin-21 AS app-build
WORKDIR /app

# Use the full path for pom.xml copy to avoid conflicts with root
COPY app/pom.xml .
COPY app/src ./src

# Build the 'app' module
RUN mvn clean package -DskipTests

# --- STAGE 2: BUILD THE 'CONSUMER' SERVICE ---
FROM maven:3.9-eclipse-temurin-21 AS consumer-build
WORKDIR /consumer

# Use the full path for pom.xml copy
COPY consumer/pom.xml .
COPY consumer/src ./src

# Build the 'consumer' module
RUN mvn clean package -DskipTests

# --- STAGE 3: MINIMAL RUNTIME ---
# OPTIMIZATION: Use the slim JRE-Alpine image for a much smaller final container (Good practice!)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /services

# CRITICAL FIX: Use '*.jar' wildcard to correctly copy the built artifacts
COPY --from=app-build /app/target/*.jar ./app.jar
COPY --from=consumer-build /consumer/target/*.jar ./consumer.jar

# Copy the start script
COPY start.sh .

# CRITICAL FIX FOR "NOT FOUND" ERROR:
# Install dos2unix (via apk) and convert line endings from CRLF to LF
RUN apk add --no-cache dos2unix && dos2unix start.sh

# Make it executable
RUN chmod +x start.sh

# The hosting platform (like Railway/Heroku) sets the dynamic $PORT environment variable.
ENV PORT 8080
EXPOSE 8080

# Use the cleaner Exec form entry point
CMD ["./start.sh"]
