# Laboratory 8 

Программа авторизации пользователей для доступа к ресурсам с проверкой ролей и объёмов.

## Требования

- **Maven 3.8+**
- **Kotlin 1.9.20**
- **Kotlinx CLI 0.3.6**
- **Kotlinx Coroutines 1.8.0**
- **Spring Boot 3.2.0**
- **H2 Database 2.2.224** (встроенная, используется автоматически)
- **Flyway Core** (для миграций базы данных)
- Все зависимости управляются через Maven (`spring-boot-dependencies`).

## Сборка и запуск

### 1. Сборка

```bash
mvn clean package
```
### 2. Запуск через Maven с аргументами
```bash
mvn spring-boot:run \
  -Dspring-boot.run.arguments="-l user1 -p pass1 -r A -o READ -v 10"
```
### 3. Запуск через JAR с аргументами
```bash
java -jar target/5_Semester_HighLevelProgramming_Maven-1.0-SNAPSHOT.jar \
  -l user1 -p pass1 -r A -o READ -v 10
```
## Аргументы программы
| Опция              | Обязательная | Тип                | Описание            |
| ------------------ | ------------ | ------------------ | ------------------- |
| -l, --login        | да           | String             | Логин пользователя  |
| -p, --password     | да           | String             | Пароль пользователя |
| -r, --resourcePath | да           | String             | Путь ресурса        |
| -o, --role         | да           | READ/WRITE/EXECUTE | Роль для доступа    |
| -v, --volume       | да           | Int                | Объём ресурса       |
## Seed данные (H2)
пользователи:

| login | password |
| ----- | -------- |
| user1 | pass1    |
| user2 | pass2    |
Ресурсы:
```bash
A (max 100), A.B (50), A.B.C (20), D.E (10)
```
Пример прав:
```bash
user1 → READ на A, WRITE на A.B
user2 → EXECUTE на A.B.C
```
## test.sh
Дать права на исполнение:
```bash
chmod +x test.sh
```
Запустить
```bash
./test.sh
```
Вывод примерно такой:
```bash
Test [user1 A READ 10]: OK
Test [userX A READ 10]: OK
...
Successful tests: 10/10
```