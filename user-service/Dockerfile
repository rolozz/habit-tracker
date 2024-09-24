# Используем официальный образ OpenJDK
FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY target/userservice.jar userservice.jar
ENTRYPOINT ["java", "-jar", "/userservice.jar"]