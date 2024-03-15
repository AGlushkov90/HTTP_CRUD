# **HTTP_CRUD**
## **Описание**
Необходимо реализовать REST API, которое взаимодействует с БД по протоколу HTTP и позволяет выполнять все CRUD операции над сущностями:

User -> Integer id, String name, List<Event> events
Event -> Integer id, User user, File file
File -> Integer id, String name, String filePath

## **Запуск проекта**
Запуск docker-compose файла "docker-compose up"
Запуск плагина liquibase для maven "mvn flyway:migrate"
