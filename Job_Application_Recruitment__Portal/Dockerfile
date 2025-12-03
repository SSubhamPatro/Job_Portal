#FROM eclipse-temurin:21-jdk-alpine
#WORKDIR /app
#
#COPY target/Job_Application_Recruitment__Portal-0.0.1-SNAPSHOT.jar app.jar
#
#EXPOSE 9095
#
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]

## --- Stage 1: Build with Maven + JDK 21 ---
#FROM maven:3.9.6-eclipse-temurin-21 AS build
#WORKDIR /app
#
## Copy only pom.xml first (for dependency caching)
#COPY pom.xml .
#RUN mvn dependency:go-offline
#
## Copy the rest of the project
#COPY src ./src
#
## Build the Spring Boot JAR
#RUN mvn clean package -DskipTests
#
## --- Stage 2: Run the Application ---
#FROM eclipse-temurin:21-jdk-alpine
#WORKDIR /app
#
## Copy jar from the build stage
#COPY --from=build /app/target/*.jar app.jar
#
#EXPOSE 9095
#
#ENTRYPOINT ["java", "-jar", "app.jar"]

# --- Stage 1: Build with Maven + JDK 21 ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml first (for dependency caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build Spring Boot JAR
RUN mvn clean package -DskipTests

# --- Stage 2: Run the Application ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy built jar
COPY --from=build /app/target/*.jar app.jar

EXPOSE 9095

ENTRYPOINT ["java", "-jar", "app.jar"]

