FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY target/Job_Application_Recruitment__Portal-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9095

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
