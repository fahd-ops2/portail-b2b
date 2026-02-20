# Build stage
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=build /app/target/portal-rh-0.0.1.jar app.jar

EXPOSE 8090

ENTRYPOINT ["java", "-Xms128m", "-Xmx256m", "-jar", "app.jar"]