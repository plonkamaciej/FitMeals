# Stage 1: Build the application
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-alpine
LABEL authors="michk"
COPY --from=build /app/target/FitMeals-0.0.1-SNAPSHOT.jar /app/FitMeals.jar
ENTRYPOINT ["java", "-jar", "/app/FitMeals.jar"]
EXPOSE 8081