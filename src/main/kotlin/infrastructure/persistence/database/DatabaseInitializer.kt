package infrastructure.persistence.database

import domain.exceptions.DatabaseConnectionException
import domain.exceptions.SqlQueryException
import java.sql.DriverManager

object DatabaseInitializer {
    private const val URL = "jdbc:h2:./database/authdb"
    private const val USER = "sa"
    private const val PASSWORD = ""

    init {
        try {
            initializeDatabase()
            seedData()
        } catch (e: DatabaseConnectionException) {
            System.err.println("Ошибка подключения к базе данных: ${e.message}")
            throw e
        } catch (e: SqlQueryException) {
            System.err.println("Ошибка SQL-запроса: ${e.message}")
            throw e
        } catch (e: Exception) {
            System.err.println("Неизвестная ошибка инициализации базы данных: ${e.message}")
            throw DatabaseConnectionException("Неизвестная ошибка инициализации базы данных", e)
        }
    }

    private fun getConnection() = try {
        DriverManager.getConnection(URL, USER, PASSWORD)
    } catch (e: Exception) {
        throw DatabaseConnectionException("Не удалось установить соединение с базой данных: ${e.message}", e)
    }

    private fun initializeDatabase() {
        getConnection().use { connection ->
            try {
                executeSqlFile(connection, "init/create_tables.sql")
                println("✅ Таблицы базы данных успешно созданы")
            } catch (e: Exception) {
                throw SqlQueryException("Ошибка создания таблиц базы данных", e)
            }
        }
    }

    private fun seedData() {
        getConnection().use { connection ->
            try {
                // Проверяем, есть ли уже данные
                val checkUsers = "SELECT COUNT(*) as count FROM users"
                val resultSet = connection.createStatement().executeQuery(checkUsers)
                resultSet.next()
                val userCount = resultSet.getInt("count")

                if (userCount == 0) {
                    executeSqlFile(connection, "init/insert_data.sql")
                    println("✅ Тестовые данные успешно добавлены в базу данных")
                } else {
                    println("✅ База данных уже содержит данные, пропускаем заполнение")
                }
            } catch (e: Exception) {
                throw SqlQueryException("Ошибка заполнения базы данных тестовыми данными", e)
            }
        }
    }

    private fun executeSqlFile(connection: java.sql.Connection, filePath: String) {
        val sqlContent = readSqlFile(filePath)

        connection.createStatement().use { statement ->
            sqlContent.split(";")
                .filter { it.isNotBlank() }
                .forEachIndexed { index, sql ->
                    if (sql.trim().isNotBlank()) {
                        try {
                            statement.execute(sql.trim())
                        } catch (e: Exception) {
                            throw SqlQueryException(
                                "Ошибка выполнения SQL команды №${index + 1} из файла $filePath: ${e.message}",
                                e
                            )
                        }
                    }
                }
        }
    }

    private fun readSqlFile(filePath: String): String = try {
        this::class.java.classLoader
            .getResource("sql/$filePath")
            ?.readText()
            ?: throw SqlQueryException("SQL файл не найден: $filePath")
    } catch (e: Exception) {
        throw SqlQueryException("Ошибка чтения SQL файла: $filePath", e)
    }
}