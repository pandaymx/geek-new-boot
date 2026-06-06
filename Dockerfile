# Stage 1: Extract layers using Spring Boot layertools
FROM eclipse-temurin:25-jdk-jammy AS builder

WORKDIR /builder

# Copy the pre-built JAR from the GitHub Actions context
# We assume the JAR is built into build/libs and has a specific naming convention
# Spring Boot typically creates a single executable jar.
COPY build/libs/*.jar application.jar

# Extract layers
RUN java -Djarmode=layertools -jar application.jar extract

# Stage 2: Create the final minimal layered image
FROM eclipse-temurin:25-jre-jammy

WORKDIR /app

# Copy the extracted layers in the correct order to maximize Docker layer caching
COPY --from=builder /builder/dependencies/ ./
COPY --from=builder /builder/spring-boot-loader/ ./
COPY --from=builder /builder/snapshot-dependencies/ ./
COPY --from=builder /builder/application/ ./

# Create a non-root user for security (optional but recommended for production)
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
