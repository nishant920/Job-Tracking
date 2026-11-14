# ==========================================
# Stage 1: Build Stage
# ==========================================
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy pom.xml first to take advantage of Docker layer caching
COPY pom.xml .

# Download dependencies offline (fails gracefully if dynamic plugins are needed at compile time)
RUN mvn dependency:go-offline -B || true

# Copy source code and package the Spring Boot executable JAR
COPY src ./src
RUN mvn clean package -DskipTests && \
    cp target/track-site-*.jar app.jar

# ==========================================
# Stage 2: Minimal Production Runtime Stage
# ==========================================
FROM eclipse-temurin:17-jre-alpine AS runner
WORKDIR /app

# Security: Run as a non-privileged system user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy only the compiled JAR from the builder stage
COPY --from=builder --chown=appuser:appgroup /app/app.jar app.jar

# Switch to non-root user
USER appuser

# Expose the application port
EXPOSE 7070

# Default environment configuration (can be overridden at runtime)
ENV PORT=7070 \
    SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# Health check leveraging Spring Boot Actuator endpoint
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:${PORT:-7070}/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
