#Stage 1: Build the 'app' service

#Using the standard, robust tag for Maven 3.9 (latest) and JDK 21 (Eclipse Temurin)

FROM maven:3.9-eclipse-temurin-21 AS app-build
WORKDIR /app
COPY app/pom.xml .
COPY app/src ./src

#Build the application, skipping tests to speed up the container build

RUN mvn clean package -DskipTests

#Stage 2: Build the 'consumer' service

FROM maven:3.9-eclipse-temurin-21 AS consumer-build
WORKDIR /consumer
COPY consumer/pom.xml .
COPY consumer/src ./src

#Build the application, skipping tests

RUN mvn clean package -DskipTests

#Stage 3: Minimal Runtime

#Using a smaller JDK runtime image for the final container

FROM eclipse-temurin:21-jdk
WORKDIR /services

#Copy built jars from the previous stages

COPY --from=app-build /app/target/.jar ./app.jar
COPY --from=consumer-build /consumer/target/.jar ./consumer.jar

#Copy the start script and make it executable

COPY start.sh .
RUN chmod +x start.sh

#Start both services using the start.sh script

CMD ["sh", "start.sh"]