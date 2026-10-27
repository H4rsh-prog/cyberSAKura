FROM eclipse-temurin:17

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

EXPOSE 8002

ENTRYPOINT ["java", "-jar", "LZ77-0.0.1-SNAPSHOT.jar"]
