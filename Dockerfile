FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/microservicio-mascotas-0.0.1-SNAPSHOT.jar app.jar

COPY wallet ./wallet

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]