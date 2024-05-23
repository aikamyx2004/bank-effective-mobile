FROM openjdk:22-ea-17-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the builder stage to the runtime image
COPY /target/bank-0.0.1-SNAPSHOT.jar .

# Expose the port the application runs on
EXPOSE 8080

# Specify the command to run the application
CMD ["java", "-jar", "bank-0.0.1-SNAPSHOT.jar"]
