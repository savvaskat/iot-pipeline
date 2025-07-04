# Use a lightweight OpenJDK 17 base image
FROM registry.access.redhat.com/ubi9/openjdk-17-runtime:1.21

# Set working directory
WORKDIR /work/

# Copy the packaged application (after mvn package -DskipTests)
COPY target/quarkus-app/lib/ /work/lib/
COPY target/quarkus-app/*.jar /work/
COPY target/quarkus-app/app/ /work/app/
COPY target/quarkus-app/quarkus/ /work/quarkus/

# (Optional) Copy config file if you're not using environment variables for everything
COPY src/main/resources/application.properties /work/config/

# JVM options (needed for Java 21+ thread-local-reset issue)
ENV JAVA_OPTIONS="-Dquarkus.config.location=/work/config/application.properties --add-opens=java.base/java.lang=ALL-UNNAMED"

# Run the application
CMD ["java", "-jar", "quarkus-run.jar"]
