FROM maven:3.9.11-amazoncorretto-21 AS dependency-builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

FROM maven:3.9.11-amazoncorretto-21 AS builder
WORKDIR /app
COPY --from=dependency-builder /root/.m2 /root/.m2
COPY src src
COPY pom.xml .
RUN mvn package -DskipTests -B

FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "--enable-preview", "-XX:+UseG1GC", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]