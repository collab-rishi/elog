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
# FIX: Swapping to the JRE-Jammy (Debian-based) image
# This provides the necessary 'bash' shell for the 'wait -n' command in start.sh
FROM eclipse-temurin:21-jre-jammy
WORKDIR /services

# Copy the built artifacts
COPY --from=app-build /app/target/*.jar ./app.jar
COPY --from=consumer-build /consumer/target/*.jar ./consumer.jar

# Copy the start script
COPY start.sh .

# Make it executable
RUN chmod +x start.sh

# The hosting platform (like Railway/Heroku) sets the dynamic $PORT environment variable.
ENV PORT 8080
EXPOSE 8080

# FIX: Explicitly execute the script using the full path to /bin/bash.
# This ensures Bash is used and resolves the "not found" error.
CMD ["/bin/bash", "./start.sh"]
