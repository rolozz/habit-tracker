# Используем официальный образ OpenJDK как базовый
FROM openjdk:17

# Создаем рабочую директорию в контейнере
WORKDIR /app

# Копируем jar-файл микросервиса в контейнер
COPY build/libs/eureka-server-0.0.1-SNAPSHOT.jar /app/eureka-server.jar

# Указываем команду для запуска микросервиса
ENTRYPOINT ["java", "-jar", "/app/eureka-server.jar"]