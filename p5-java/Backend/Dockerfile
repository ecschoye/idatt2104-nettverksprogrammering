# Use the eclipse-temurin:17-jdk-alpine image as the base image
FROM eclipse-temurin:17-jdk-alpine

# Create a mount point for the temporary volume in the container
VOLUME /tmp

# Declare a build-time argument called JAR_FILE
ARG JAR_FILE

# Copy the JAR file specified by the JAR_FILE argument to the app.jar file in the container
COPY ${JAR_FILE} app.jar

# Set the default command to run when the container starts, which launches the Java application from the app.jar file
ENTRYPOINT ["java","-jar","/app.jar"]
