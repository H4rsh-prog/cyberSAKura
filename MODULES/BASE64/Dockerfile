FROM eclipse-temurin:17

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

EXPOSE 8001

ENTRYPOINT ["java", "-jar", "BASE64-0.0.1-SNAPSHOT.jar"]
