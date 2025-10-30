# --- STAGE 1: BUILD THE 'CONSUMER' SERVICE ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /consumer

# Copy the pom and source code
COPY pom.xml .
COPY src ./src

# Build the 'consumer' module
RUN mvn clean package -DskipTests

# --- STAGE 2: MINIMAL RUNTIME ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /consumer

# Copy the built artifact
COPY --from=build /consumer/target/*.jar ./consumer.jar

# Since this is a consumer/worker, it typically does not need a public port.
# If it needs a health check endpoint, you can keep the port logic.
# If not, you can simply run it as a background worker.

# The service will run only this JAR.
CMD ["java", "-jar", "./consumer.jar"]