# Use a lightweight OpenJDK base image
FROM registry.access.redhat.com/ubi9/openjdk-17-runtime:1.21

# Set the working directory inside the container
WORKDIR /work/

# Copy the built Quarkus application (after mvn package)
COPY target/quarkus-app/lib/ /work/lib/
COPY target/quarkus-app/*.jar /work/
COPY target/quarkus-app/app/ /work/app/
COPY target/quarkus-app/quarkus/ /work/quarkus/
COPY src/main/resources/application.properties /work/config/

# Set configuration location
ENV JAVA_OPTIONS="-Dquarkus.config.location=/work/config/application.properties"

# Expose port (for documentation only)
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "quarkus-run.jar"]