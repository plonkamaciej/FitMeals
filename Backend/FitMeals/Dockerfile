FROM ubuntu:latest
LABEL authors="michk"
VOLUME /tmp
COPY target/your-app-name.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]