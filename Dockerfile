# Use Gradle with OpenJDK 17 to build the application
FROM gradle:7.6-jdk17 AS build

# Set the working directory inside the container
WORKDIR /Email-Generator

# Copy all the application files into the container
COPY . .

# Run Gradle clean build to generate the JAR file
RUN gradle clean build --no-daemon

# List the files in the libs directory to verify the JAR file exists
RUN ls /Email-Generator/build/libs/

# Use a minimal JDK image for the runtime
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the build image
# Make sure that the JAR file exists in /Email-Generator/build/libs/
COPY --from=build /Email-Generator/build/libs/Email-Generator-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port (if your app uses port 8080)
EXPOSE 8080

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
