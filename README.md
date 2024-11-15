[<img src="https://run.pstmn.io/button.svg" alt="Run In Postman" style="width: 128px; height: 32px;">](https://app.getpostman.com/run-collection/31978243-0f915346-d37a-41cc-ab5f-2de7d39b4939?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D31978243-0f915346-d37a-41cc-ab5f-2de7d39b4939%26entityType%3Dcollection%26workspaceId%3D51272742-5e39-48c4-a588-b73f889a4d56)

# Инструкция по сборке и запуску проекта

## Требования

Для сборки и запуска проекта необходимы следующие инструменты:

- **JDK 21 или выше**: проверьте, что команда `java --version` показывает установленную версию JDK 17 или выше.
- **Gradle** (опционально): встроенный скрипт Gradle (`gradlew`) уже включен в проект.
- **Git**: для клонирования репозитория.
- **Операционная система**: Windows, macOS или Linux.

## Установка базы данных PostgreSQL

1. **Скачайте и установите PostgreSQL**:
   - Перейдите на [официальный сайт PostgreSQL](https://www.postgresql.org/download/) и скачайте подходящую версию для вашей ОС.
   - Установите PostgreSQL, следуя инструкциям установщика. Запомните логин, пароль и имя создаваемой базы данных.

2. **Создайте базу данных**:
   После установки PostgreSQL откройте консоль `psql` или другой инструмент управления (например, PgAdmin) и выполните следующие команды:
   ```sql
   CREATE DATABASE token_price_api;
   CREATE USER api_user WITH PASSWORD 'secure_password';
   GRANT ALL PRIVILEGES ON DATABASE token_price_api TO api_user;

Замените secure_password на сложный пароль.

## Настройка подключения к базе данных

1. Откройте файл конфигурации приложения (например, `application.yml` или `application.properties` в зависимости от проекта).

2. Добавьте или измените настройки подключения:

   Для `application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/token_price_api
       username: api_user
       password: secure_password
     jpa:
       hibernate:
         ddl-auto: update
   ```

   Для `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/token_price_api
   spring.datasource.username=api_user
   spring.datasource.password=secure_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. Убедитесь, что вы указали правильный порт и данные пользователя.

## Сборка проекта

1. **Клонируйте репозиторий:**

   ```bash
   git clone git@github.com:Yooshyasha/TokenPriceAPI.git
   cd TokenPriceAPI

2. **Соберите проект с помощью Gradle:**

   Для Windows:
   ```bash
   gradlew.bat build
   ```

   Для macOS/Linux:
   ```bash
   ./gradlew build
   ```

   Это создаст исполняемый JAR-файл в папке `build/libs`.

## Запуск проекта

1. Перейдите в папку с собранным JAR-файлом:

   ```bash
   cd build/libs
   ```

2. Запустите приложение:

   ```bash
   java -jar <имя-файла>.jar
   ```

   Замените `<имя-файла>.jar` на фактическое имя сгенерированного JAR-файла (например, `myapp-1.0-SNAPSHOT.jar`).

## Примечания

- Если при запуске возникают ошибки, убедитесь, что все зависимости были корректно установлены.
- Для изменения конфигурации проекта используйте файлы `build.gradle.kts` и `settings.gradle.kts`.

## Полезные команды Gradle

- **Очистить проект:**
  ```bash
  ./gradlew clean
  ```

- **Запустить приложение (если задана основная функция):**
  ```bash
  ./gradlew run
  ``