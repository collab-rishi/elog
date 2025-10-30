# --- STAGE 1: BUILD THE 'APP' SERVICE ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom and source code
COPY pom.xml .
COPY src ./src

# Build the 'app' module
RUN mvn clean package -DskipTests

# --- STAGE 2: MINIMAL RUNTIME ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the built artifact
COPY --from=build /app/target/*.jar ./app.jar

# Railway automatically sets $PORT. We run the app on the port Railway assigns.
# Ensure your Spring Boot configuration uses ${PORT} or an equivalent.
ENV SERVER_PORT $PORT

# The service will run only this JAR.
# The `java -jar` command becomes the CMD instruction.
CMD ["java", "-jar", "./app.jar"]