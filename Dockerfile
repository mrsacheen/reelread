# Use Azul Zulu OpenJDK 18 as a base image
FROM azul/zulu-openjdk-alpine:18

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . .

# Package the application as a jar file
RUN ./mvnw package -DskipTests

# Make port 8080 available to the world outside this container
EXPOSE 3661

# Run the jar file
CMD ["java", "-jar", "target/ReelRead-0.0.1-SNAPSHOT.jar"]