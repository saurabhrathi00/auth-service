# Use a small, supported JDK image
FROM eclipse-temurin:17-jdk

# Metadata (optional)
LABEL maintainer="saurabhrathi00"

# Create non-root user for better security (Debian/Ubuntu style)
RUN groupadd appgroup && useradd -m -g appgroup appuser

# Working directory
WORKDIR /app

# Copy the fat jar (local path on build machine)
# Adjust filename if your artifactId/version differs
COPY target/auth-service-0.0.1-SNAPSHOT.jar ./auth-service.jar

# Copy config & secrets into /etc inside container
# These are read-only inside container
COPY configs/service.properties /etc/service.properties
COPY secrets/secrets.properties /etc/secrets.properties

# Expose the application port
EXPOSE 8080

# Set environment so Spring Boot reads external property files.
# We use file: prefix so Spring treats them as files (not classpath)
ENV SPRING_CONFIG_LOCATION=file:/etc/service.properties,file:/etc/secrets.properties

# Run as non-root user
USER appuser

# Start the app
ENTRYPOINT ["java", "-jar", "/app/auth-service.jar"]
