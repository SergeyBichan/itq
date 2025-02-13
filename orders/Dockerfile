FROM maven:3.8.5-openjdk-17-slim AS builder
WORKDIR /usr/src/
COPY . .
RUN mvn install -Dmaven.test.skip

FROM openjdk:17-alpine
WORKDIR /app
COPY --from=builder /usr/src/target/*.jar orders.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "orders.jar"]