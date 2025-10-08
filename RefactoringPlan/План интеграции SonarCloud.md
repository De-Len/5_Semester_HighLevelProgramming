# План интеграции SonarCloud

Этот план описывает пошаговую интеграцию статического анализатора кода SonarCloud для проекта на Kotlin, без использования систем сборки типа Gradle или Maven. Анализ будет выполняться с помощью SonarScanner CLI.

## 1. Подготовка к интеграции

1.  **Создание аккаунта SonarCloud:**
    *   Перейдите на [SonarCloud](https://sonarcloud.io/) и зарегистрируйтесь, используя аккаунт GitHub, GitLab, Bitbucket или Azure DevOps.

2.  **Создание организации и проекта на SonarCloud:**
    *   После входа создайте новую организацию (если у вас ее еще нет).
    *   Внутри организации создайте новый проект. Выберите опцию 

    *   Внутри организации создайте новый проект. Выберите опцию `Analyze a new project` и следуйте инструкциям. Для проекта без систем сборки, возможно, потребуется выбрать `Other` или настроить вручную.

3.  **Генерация токена SonarCloud:**
    *   В SonarCloud перейдите в `My Account` -> `Security` и сгенерируйте новый токен. Сохраните его в безопасном месте, он понадобится для аутентификации SonarScanner.

4.  **Установка SonarScanner CLI:**
    *   SonarScanner CLI — это универсальный инструмент командной строки для запуска анализа. Скачайте его с официального сайта SonarSource: [SonarScanner CLI](https://docs.sonarcloud.io/getting-started/sonarscanner-cli/).
    *   Распакуйте архив в удобное место (например, `/opt/sonar-scanner`).
    *   Добавьте путь к директории `bin` SonarScanner в переменную окружения `PATH` или используйте полный путь к исполняемому файлу.

## 2. Настройка проекта для анализа

1.  **Создание файла `sonar-project.properties`:**
    *   В корневой директории вашего проекта (например, `/home/ubuntu/lab2_project/5_Semester_HighLevelProgramming2Lab/`) создайте файл с именем `sonar-project.properties`.
    *   Добавьте в него следующие параметры:

    ```properties
    # Required metadata
    sonar.projectKey=YourOrganization_YourProjectKey # Замените на ключ вашего проекта в SonarCloud
    sonar.organization=YourOrganizationID # Замените на ID вашей организации в SonarCloud
    sonar.projectName=5_Semester_HighLevelProgramming2Lab
    sonar.projectVersion=1.0

    # Path to the sources (for example, src/main/java, src/main/kotlin)
    sonar.sources=src/main/kotlin

    # Encoding of the source files
    sonar.sourceEncoding=UTF-8

    # Exclude build directories and other generated files
    sonar.exclusions=build/**, .gradle/**

    # Language of the project
    sonar.language=kotlin

    # Path to the binaries (compiled classes). SonarScanner needs this to analyze bytecode.
    # Since we are not using Gradle/Maven, you might need to compile your Kotlin code manually
    # and point this to the directory containing .class files. If not compiling to .class, 
    # SonarCloud will perform source code analysis only.
    # Example: sonar.java.binaries=build/classes/kotlin/main
    # For pure source analysis, this might not be strictly necessary if you don't have compiled artifacts.
    # However, for deeper analysis (e.g., detecting issues in compiled code), it's recommended.
    # If you are compiling manually, ensure the output directory is specified here.
    # For this specific project, given the constraint 

    # For this specific project, given the constraint "Использование систем сборки запрещено", 
    # we will focus on source code analysis. If compilation is done via `kotlinc`, 
    # you would point `sonar.java.binaries` to the output directory of `.class` files.
    # For now, we will omit `sonar.java.binaries` for a source-only analysis.
    ```

    **Важно:** Замените `YourOrganization_YourProjectKey` и `YourOrganizationID` на актуальные значения из вашего аккаунта SonarCloud.

2.  **Компиляция Kotlin кода (если требуется для более глубокого анализа):**
    *   Если вы хотите, чтобы SonarCloud анализировал байт-код (что дает более полный анализ), вам нужно скомпилировать ваш Kotlin код вручную с помощью `kotlinc`.
    *   Пример команды для компиляции:
        ```bash
        kotlinc src/main/kotlin -d build/classes/kotlin/main
        ```
    *   В этом случае, в `sonar-project.properties` нужно будет добавить `sonar.java.binaries=build/classes/kotlin/main`.
    *   **Примечание:** В соответствии с нефункциональным требованием "Использование систем сборки запрещено", мы будем ориентироваться на анализ исходного кода, поэтому этот шаг может быть пропущен, если не требуется анализ байт-кода.

## 3. Запуск анализа

1.  **Выполнение SonarScanner CLI:**
    *   Перейдите в корневую директорию вашего проекта, где находится `sonar-project.properties`.
    *   Выполните следующую команду, используя ваш токен SonarCloud:

        ```bash
        sonar-scanner \
            -Dsonar.token=YOUR_SONARCLOUD_TOKEN
        ```

        **Важно:** Замените `YOUR_SONARCLOUD_TOKEN` на токен, который вы сгенерировали в SonarCloud.

2.  **Просмотр результатов:**
    *   После завершения анализа (это может занять несколько минут), SonarScanner выведет ссылку на отчет в SonarCloud.
    *   Перейдите по этой ссылке, чтобы просмотреть результаты анализа вашего кода, включая найденные уязвимости, ошибки, запахи кода и покрытие тестами (если настроено).

## 4. Интеграция в CI/CD (опционально, но рекомендуется)

Хотя в задании не требуется CI/CD, для автоматизации процесса анализа рекомендуется интегрировать SonarScanner в вашу систему непрерывной интеграции (например, GitHub Actions, GitLab CI/CD, Jenkins). Это позволит автоматически запускать анализ при каждом изменении кода и получать обратную связь о качестве кода.

### Пример для GitHub Actions (файл `.github/workflows/sonarcloud.yml`):

```yaml
name: Build and Analyze

on:
  push:
    branches:
      - main
      - develop
  pull_request:
    types: [opened, synchronize, reopened]

jobs:
  build:
    name: Build and Analyze
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
        with:
          fetch-depth: 0  # Shallow clones disable SonarCloud analysis

      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: 17
          distribution: 'temurin'

      - name: Install SonarScanner CLI
        run: |
          wget https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-5.0.1.3006-linux.zip
          unzip sonar-scanner-cli-5.0.1.3006-linux.zip
          echo "$(pwd)/sonar-scanner-5.0.1.3006-linux/bin" >> $GITHUB_PATH

      - name: Run SonarScanner
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}  # Needed to get PR information, if any
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }} # SonarCloud token
        run: |
          sonar-scanner \
            -Dsonar.projectKey=YourOrganization_YourProjectKey \
            -Dsonar.organization=YourOrganizationID \
            -Dsonar.sources=src/main/kotlin \
            -Dsonar.language=kotlin \
            -Dsonar.token=${{ secrets.SONAR_TOKEN }}
```

**Важно:**
*   Добавьте `SONAR_TOKEN` в секреты вашего репозитория GitHub.
*   Замените `YourOrganization_YourProjectKey` и `YourOrganizationID` на актуальные значения.
*   Убедитесь, что `java-version` соответствует версии JDK, используемой для компиляции вашего Kotlin-кода.

Этот план предоставляет основу для интеграции SonarCloud в ваш проект, позволяя поддерживать высокое качество кода и выявлять потенциальные проблемы на ранних стадиях разработки.
