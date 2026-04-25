FROM maven:3.9-eclipse-temurin-17
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests -q
ENTRYPOINT ["java", "-jar", "target/weather-publisher-0.0.1-SNAPSHOT.jar"]