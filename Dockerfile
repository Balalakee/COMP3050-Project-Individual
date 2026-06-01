FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests clean package

FROM eclipse-temurin:17-jre-alpine

RUN rm -f /usr/bin/pebble

WORKDIR /app

COPY --from=build /app/target/comp3050-project-1.0-SNAPSHOT.jar app.jar
COPY public ./public

EXPOSE 8000

CMD ["java", "-jar", "app.jar"]