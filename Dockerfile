# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the built jar from the target directory
COPY target/fraud-detection-engine-1.0-SNAPSHOT.jar app.jar

ENV SPRING_CONFIG_LOCATION=/app/config/application.properties

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
