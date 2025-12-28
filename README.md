# Laboratory 8 

Программа авторизации пользователей для доступа к ресурсам с проверкой ролей и объёмов.

## Требования

- JDK 17 (рекомендовано)
- Maven 3.8+
- H2 (встроенная, используется автоматически)

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
