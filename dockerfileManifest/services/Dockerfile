FROM eclipse-temurin:17

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

EXPOSE 1337

ENTRYPOINT ["java", "-jar", "test-0.0.1-SNAPSHOT.jar"]
