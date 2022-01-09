FROM gradle:latest AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon


FROM openjdk:8-jdk-alpine

LABEL maintainer="god"

EXPOSE 8080

COPY --from=build /home/gradle/src/build/libs/backendapplication-0.0.1-SNAPSHOT.war /backendapplication.war

ENTRYPOINT ["java","-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap","-Djava.security.egd=file:/dev/./urandom","-jar","/backendapplication.war"]