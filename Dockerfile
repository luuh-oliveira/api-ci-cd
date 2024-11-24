FROM maven:3-openjdk-17 as builder
LABEL authors="luoliveira"

WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests -Dcheckstyle.skip=true

FROM openjdk:17-slim

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]