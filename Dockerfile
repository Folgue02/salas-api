FROM openjdk:17-ea-6-slim
EXPOSE 8080
COPY target/salas-0.0.1-SNAPSHOT.jar /app/salas-api.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "salas-api.jar"]
