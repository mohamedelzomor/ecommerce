# Use an official Java runtime as a parent image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy Maven wrapper and project files
COPY . .

# Build the project (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# Expose the port your app runs on
EXPOSE 8080

# Run the Spring Boot jar
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]