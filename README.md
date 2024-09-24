# Проект: Habit Tracker

## Описание проекта

Этот проект представляет собой микросервисное приложение для отслеживания привычек. Оно включает в себя несколько микросервисов, каждый из которых отвечает за определенные функции.

## Установка и запуск

### Требования

- Java 17 или выше
- Gradle (можно установить в виде плагина в IDEA)
- Docker и Docker Compose

### Описание Docker файла (Dockerfile)

Dockerfile — это сценарий с набором инструкций, который используется для создания Docker-образа. Этот файл определяет, как будет выглядеть и функционировать контейнер с приложением. В нем описываются:

Базовый образ: Начальная точка для создания Docker-образа, в данном приложении openjdk:17.
Рабочая директория: Директория внутри контейнера, где будут храниться файлы приложения.
Копирование файлов: Перенос файлов из локальной системы в контейнер.
Команда для запуска: Основная команда, которая запускается при старте контейнера.

Основные команды для работы с Dockerfile:
  Сборка Docker-образа: docker build -t my-app .
  (Эта команда собирает Docker-образ, используя инструкции, указанные в Dockerfile. Флаг -t задает имя для образа.)
  Запуск контейнера: docker run -d -p 8080:8080 my-app
  (!Внимательно, порт указан для примера)
  (Эта команда запускает контейнер на основе созданного образа. Флаг -d запускает контейнер в фоновом режиме, а -p связывает порты локальной машины с портами контейнера.)

### Описание Docker Compose файла (docker-compose.yml)

Docker Compose — это инструмент для запуска многоконтейнерных Docker-приложений. С помощью docker-compose.yml описываются и запускаются несколько микросервисов в одном сетевом окружении. Этот файл позволяет легко управлять зависимостями между сервисами и их настройками.

В docker-compose.yml описываются:

Сервисы: Определения для каждого контейнера (например, баз данных, микросервисов).
Сетевые настройки: Конфигурация сети между сервисами.
Томa (volumes): Монтирование хранилищ данных в контейнеры.

Основные команды для работы с Docker Compose:
  Запуск всех сервисов: docker-compose up
  (Эта команда поднимает все сервисы, описанные в docker-compose.yml. Добавьте флаг -d, чтобы запустить их в фоновом режиме)
  Остановка всех сервисов: docker-compose down
  (Эта команда останавливает и удаляет все запущенные контейнеры, тома и сети, созданные с помощью Docker Compose)
  Просмотр логов сервисов: docker-compose logs
  (Эта команда выводит логи всех запущенных сервисов)

## Структура проекта

Проект состоит из нескольких модулей, каждый из которых представляет собой отдельный микросервис. Основные модули включают:
- **EurekaServer** Платформа для поиска сервисов, разработанная Netflix и широко применяемая в экосистеме микросервисов Spring.
Она предоставляет серверный компонент для регистрации служб и клиентский компонент для обнаружения служб,
позволяя микросервисам самостоятельно регистрироваться и динамически находить другие службы.
- **Auth Service**: Обрабатывает запросы на аутентификацию и авторизацию. Основная нагрузка здесь будет зависеть от количества пользователей и частоты их авторизации.
- **User Service**: Управляет данными пользователей. Нагрузка зависит от количества операций CRUD (создание, чтение, обновление, удаление) с профилями пользователей.
- **Habit Service**: Отвечает за управление привычками пользователей. Частота запросов зависит от активности пользователей и количества действий, связанных с отслеживанием привычек. 
- **Task Service**: Управляет задачами, связанными с привычками.
- **Notification Service**: Генерирует и отправляет уведомления. Нагрузка зависит от количества пользователей и настроек уведомлений.
- **API Gateway**: Центральный пункт маршрутизации запросов между клиентами и микросервисами. Нагрузка на API Gateway может быть значительной, так как он обрабатывает все входящие запросы.

Каждый модуль имеет свои собственные зависимости и настройки.
## Описание тестирования:

1. Запустить docker-compose up --build для развертывания всех микросервисов.
2. Проверить логи сервисов с помощью команды docker-compose logs.
3. Проверить доступность каждого сервиса по указанным в docker-compose.yml портам.
4. Проверьте взаимодействие между сервисами: убедитесь, что микросервисы могут корректно взаимодействовать друг с другом (например, eureka-server регистрирует другие микросервисы).

**О чем проект ?**
**Проект представляет собой** разработку приложения для отслеживания привычек (Habit Tracker). С помощью этого приложения пользователи смогут создавать, отслеживать и анализировать свои привычки, ставить цели и следить за их выполнением. Приложение предоставляет удобный интерфейс для ведения записей о выполнении ежедневных задач, связанных с формированием и поддержанием полезных привычек.

**Основная цель проекта** – помочь пользователям улучшить свою продуктивность и дисциплину, предоставляя инструмент для визуализации прогресса и мотивируя их к достижению личных целей. Habit Tracker станет надежным помощником в формировании новых привычек и улучшении повседневной жизни.

**Прогнозирование и анализ данных** о привычках пользователей проводится с использованием методов машинного обучения, что позволяет более точно прогнозировать развитие привычек и давать рекомендации. Проект построен на языке Java и использует фреймворки Spring Boot и Spring Cloud для обеспечения надежности и масштабируемости приложения.

**Как запустить проект ?**
Сменить версию Java в IDE на 17. Main menu (Alt+\) --> ProjectStructure --> SDK - 17.
Сделайте Build всего проекта. Для этого справа в IDE нажимаем на gradle () раскрыть ветку habit-tracker --> Tasks --> build --> build (если будут ошибки - ничего страшного)
Файл README.MD находится в корневом каталоге.
Docker должен быть запущен.
Внутри каждого микросервиса имеется файл SpringBootApplication, внутри которого можно запустить каждый имеющийся микросервис.
Так же, для удобства запуска микросервисов необходимо в навигационной панели слева снизу перейти по вкладку Services и если там только Docker и нет Spring Boot, то необходимо нажать: + → Run Configuration Type → Spring Boot (если там Spring Boot уже есть, то ничего дополнительно делать не нужно)
В дальнейшем этот процесс будет осуществляться через Docker Compose, запуском docker-compose-run_db.yaml.
Некоторые микросервисы не запустятся, так как им требуется подключение к базе данных, ее можно поднять локально или через Docker.
После запуска всех микросервисов, панели запуска (Alt+8) переместите модуль Eureka / EurekaServerApplication в самое начало, он должен запускаться первым.
Запустите весь проект
Как запустить один микросервис?
В каждом микросервисе есть свой docker-compose.yaml, в папке resources - запустите его, таким образом запустится данный микросервис
