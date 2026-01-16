FROM maven:3.9.6-eclipse-temurin-21 as builder
WORKDIR /app
COPY . .
RUN mvn -f backend/shared/pom.xml clean install -DskipTests
RUN mvn -f backend/command-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/backend/command-service/target/*.jar app.jar
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]
