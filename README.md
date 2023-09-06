# Explore with Me

### Проект:
<p>Приложение позволяющее делиться информацией об интересных событиях и 
помогать найти компанию для участия в них.</p>
---
Случалось ли вам планировать мероприятие, искать информацию договариваться... 
<br>С сервисом - афишей вы легко сможете предложить какое-либо событие от выставки до похода в кино !

### _Приложение включает в себя сервисы:_

- Основной сервис — содержит всё необходимое для работы.
    - Просмотр событий без авторизации;
    - Возможность создания и управления категориями;
    - События и работа с ними - создание, модерация;
    - Запросы пользователей на участие в событии - запрос, подтверждение, отклонение.
    - Создание и управление подборками.
    - Добавление и удаление Лайков событиям, формирование рейтингов.
- Сервис статистики — хранит количество просмотров и позволяет делать различные выборки для анализа работы приложения.
    - Отдельный сервис для сбора статистики;
- Фича "Комментарии".
    - Возможность оставлять комментарии к событиям и модерировать их.

### _Описание сервисов:_

#### _Основной сервис..._

#### _Сервис статистики, выделенный порт: 9090_

- **Административный** (_доступен только для администратора проекта_)
    - API для работы со статистикой посещений

#### _Фича комментарии_


### _Спецификация REST API swagger_

- [Основной сервис](https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-main-service-spec.json)
- [Сервис статистики](https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-stats-service-spec.json)

### _Postman тесты для сервисов:_

- [Основной сервис](https://github.com/yandex-praktikum/java-explore-with-me/blob/main_svc/postman/ewm-main-service.json)
- [Сервис статистики](https://github.com/yandex-praktikum/java-explore-with-me/blob/stat_svc/postman/ewm-stat-service.json)
- [Тест для фичи комментаии]()
---
### Жизненный цикл событие модели данных:
1. Создание события.
2. Ожидание публикации. В статус ожидания публикации событие переходит сразу после создания.
3. Публикация. В это состояние событие переводит администратор.
4. Отмена публикации. В это состояние событие переходит в двух случаях. Первый — если администратор решил, что его нельзя публиковать. Второй — когда инициатор события решил отменить его на этапе ожидания публикации.
---
### Стек технологий
+ [Java](https://www.java.com/)
+ [Spring Boot](https://spring.io/projects/spring-boot)
+ [Hibernate](https://hibernate.org)
+ [PostgreSQL](https://www.postgresql.org)
+ [Liquibase](https://www.liquibase.org)
+ [Docker Compose](https://www.docker.com)
+ [Apache Maven](https://maven.apache.org)
+ [Project Lombok](https://projectlombok.org)
+ [Postman](https://www.postman.com)
+ [IntelliJ IDEA](https://www.jetbrains.com/ru-ru/idea/)

---
### Запуск приложения
Потребуется Java 11, Docker, Git, Apache Maven

1. Склонировать
```shell
git clone https://github.com/AKnazzz/java-explore-with-me
```
2. Собрать проект
```shell
mvn clean package
```
3. Запустить через Docker Compose
```shell
docker compose up
```
---