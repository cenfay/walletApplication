FROM openjdk:21-slim
LABEL authors="nekit"

WORKDIR /app

COPY target/walletApplication-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
