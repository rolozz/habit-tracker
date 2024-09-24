Spring Cloud Config Server

Spring Cloud Config Server предоставляет централизованный сервер конфигурации для микросервисов, основанных на Spring Cloud.

Подключаем зависимость:

implementation 'org.springframework.cloud:spring-cloud-config-server'

Для получения конфигурации добавьте в ваш конфигурационный файл следующее:

spring:
    config:
        import: optional:configserver:http://localhost:8888
profiles:
    active: dev
application:
    name: myconfig-client-app

1) spring.config.import: адрес Config Server'а
2) profiles.active: имя профиля требуемой конфигурации(dev, prod, test). default профиль по умолчанию.
3) application.name: имя вашего приложения, по нему осуществляется поиск в базе.

Добавление конфигурации. 
Конфигурация хранится в базе данных MongoDB. Для добавления записи в БД можно использовать GUI MongoDB Compass https://www.mongodb.com/products/tools/compass 
или используя консоль в идее ввести следующее:

use test;  
db.properties.insertOne(
{
"name": "my-app-name",
"profiles": [
"dev"
],
"label": null,
"version": null,
"state": null,
"propertySources": [
{
"name": "from-config-server",
"source": {
"server.port": 8080,
"eureka.client.service-url.defaultZone": "http://localhost:8761/eureka"
}
}
]
}
)

1) use.test - создаем базу данных с именем test
2) в коллекцию properties добавляем запись
3) my-app-name меняем на имя вашего приложения
4) profiles - список профилей(один или несколько)
5) label, version, state используется для управления версиями конфигурации
6) В массиве source хранится ваша конфигурация в виде пары ключ- значение


