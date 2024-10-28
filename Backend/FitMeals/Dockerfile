FROM openjdk:17-jdk-alpine
LABEL authors="michk"
COPY target/FitMeals-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java","-jar","/app/FitMeals-0.0.1-SNAPSHOT.jar"]
EXPOSE 8081