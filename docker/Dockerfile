# Stage 1: Build the Spring Boot application
# Using gradle:8.10.2-jdk21 as the build environment as specified
FROM gradle:8.10.2-jdk21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project to the build container
# The --chown=gradle:gradle ensures the files are owned by the gradle user,
# which is the default user in the gradle image, preventing permission issues.
COPY --chown=gradle:gradle . .

# Build the application using Gradle.
# -x test is added to skip running tests during the build, which is common for CI/CD builds.
# If tests are critical and should run, remove '-x test'.
RUN gradle clean build -x test

# Stage 2: Create the final lightweight image
# Using eclipse-temurin:21-jdk-alpine as the base image for the runtime environment
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage to the current stage
# The artifactName is ai_projects-0.0.1-SNAPSHOT.jar
COPY --from=build /app/build/libs/ai_projects-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 7070

# Define the entry point to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]