# Stage 1: Build app
FROM maven:3.9.3-eclipse-temurin-21 AS app-build
WORKDIR /app
COPY app/pom.xml .
COPY app/src ./src
RUN mvn clean package -DskipTests

# Stage 2: Build consumer
FROM maven:3.9.3-eclipse-temurin-21 AS consumer-build
WORKDIR /consumer
COPY consumer/pom.xml .
COPY consumer/src ./src
RUN mvn clean package -DskipTests

# Stage 3: Runtime
FROM eclipse-temurin:21-jdk
WORKDIR /services

# Copy built jars from previous stages
COPY --from=app-build /app/target/*.jar ./app.jar
COPY --from=consumer-build /consumer/target/*.jar ./consumer.jar

# Copy start script and make it executable
COPY start.sh .
RUN chmod +x start.sh

# Start both services via your start.sh script
CMD ["sh", "start.sh"]
