FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/your-authentication-service.jar /app/auth-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "auth-service.jar"]
