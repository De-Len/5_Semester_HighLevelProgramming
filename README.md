# Resource Authorizer

CLI-утилита для аутентификации и авторизации пользователей при доступе к ресурсам.

## 🚀 Как запустить

```bash
./gradlew build
java -jar build/libs/5_Semester_HighLevelProgramming2Lab-1.0-SNAPSHOT-all.jar \
  --login user1 \
  --pass secret \
  --res A.B.C \
  --role READ \
  --vol 10
