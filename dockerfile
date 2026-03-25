# Use official Java 17 runtime
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy all project files to the container
COPY . .

# Give execution permission to Maven wrapper
RUN chmod +x mvnw

# Build the project (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# Expose the port Spring Boot app uses
EXPOSE 8080

# Run the Spring Boot jar
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]