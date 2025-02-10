# Этап сборки приложения
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . /app/
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true

# Этап выполнения приложения
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar
# Порт в докере. Приложение настроено на 8081 в проперти, но мы можем маппить его наружу на 8080
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
