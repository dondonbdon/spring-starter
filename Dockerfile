FROM gradle:8.8 AS build
WORKDIR /app
COPY gradle /app/gradle
COPY gradlew /app/
COPY build.gradle /app/
COPY settings.gradle /app/
COPY src /app/src
RUN ./gradlew build --no-daemon

FROM openjdk:21 AS runtime
LABEL authors="BAC"
WORKDIR /app
COPY --from=build app/build/libs/spring1-0.0.1-SNAPSHOT.jar /app/spring1.jar
EXPOSE 51001
ENTRYPOINT ["java", "-jar", "spring1.jar"]

