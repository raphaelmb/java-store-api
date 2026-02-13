# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Analyze dependencies and create custom JRE with jlink
FROM eclipse-temurin:21-jdk-alpine AS jre-build
WORKDIR /app

# Copy the built jar
COPY --from=build /app/target/*.jar app.jar

# Install Maven (needed for dependency:copy-dependencies)
RUN apk add --no-cache maven

# Copy pom.xml to download dependencies
COPY --from=build /app/pom.xml .

# Copy dependencies, analyze with jdeps, and create custom JRE with jlink
RUN mvn dependency:copy-dependencies -DoutputDirectory=deps && \
    jdeps --ignore-missing-deps -q --multi-release 21 --print-module-deps \
          --class-path 'deps/*' app.jar > jre-deps.info && \
    jlink --add-modules $(cat jre-deps.info) \
          --strip-debug \
          --no-man-pages \
          --no-header-files \
          --compress=2 \
          --output /custom-jre

# Stage 3: Final minimal runtime image
FROM alpine:latest
WORKDIR /app

# Copy custom JRE
COPY --from=jre-build /custom-jre /opt/java

# Copy application jar
COPY --from=build /app/target/*.jar app.jar

# Add required libraries for Java on Alpine
RUN apk add --no-cache libc6-compat

# Set Java in PATH
ENV PATH="/opt/java/bin:${PATH}"
ENV JAVA_HOME="/opt/java"

# Expose port (adjust as needed)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]