#!/bin/bash
# Скрипт для тестирования Resource Authorizer

JAR_NAME="5_Semester_HighLevelProgramming_Maven-1.0-SNAPSHOT.jar"
TARGET_DIR="target"

# Проверка, что JAR существует
if [ ! -f "$TARGET_DIR/$JAR_NAME" ]; then
  echo "JAR-файл не найден. Сначала соберите проект через mvn package"
  exit 1
fi

# Массив тестов
# Формат: "login password resource role volume expected_exit_code"
tests=(
  "user1 pass1 A READ 10 0"        # SUCCESS
  "userX pass1 A READ 10 1"        # INVALID_LOGIN
  "user1 wrongpass A READ 10 2"    # INVALID_PASSWORD
  "user1 pass1 Z READ 10 3"        # RESOURCE_NOT_FOUND
  "user2 pass2 A WRITE 10 4"       # ACCESS_DENIED
  "user1 pass1 A READ 200 5"       # EXCEEDED_MAX_VOLUME
  "user1 pass1 A INVALID 10 6"     # UNKNOWN_ACTION (неверная роль)
  "user2 pass2 D.E EXECUTE 10 4"   # ACCESS_DENIED (тест с другим ресурсом)
  "user2 pass2 A.B.C EXECUTE 15 5" # EXCEEDED_MAX_VOLUME (превышение)
  "user2 pass2 A.B.C EXECUTE 10 0" # SUCCESS
)

# Счетчик успешных тестов
success=0
total=${#tests[@]}

run_test() {
  local login=$1
  local password=$2
  local resource=$3
  local role=$4
  local volume=$5
  local expected=$6

  java -jar "$TARGET_DIR/$JAR_NAME" -l "$login" -p "$password" -r "$resource" -o "$role" -v "$volume"
  actual=$?

  if [ "$actual" -eq "$expected" ]; then
    echo "Test [$login $resource $role $volume]: OK"
    ((success++))
  else
    echo "Test [$login $resource $role $volume]: FAIL (expected $expected, got $actual)"
  fi
}

# Запуск всех тестов
for test_case in "${tests[@]}"; do
  run_test $test_case
done

echo "--------------------------------------------------"
echo "Successful tests: $success/$total"