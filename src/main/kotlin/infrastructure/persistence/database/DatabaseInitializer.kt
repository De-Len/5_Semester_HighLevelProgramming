package infrastructure.persistence.database

import database.Permission
import database.Resource
import database.User
import infrastructure.services.HashingService
import domain.enums.Role
import java.sql.DriverManager

object DatabaseInitializer {
    private const val URL = "jdbc:h2:./database/authdb"
    private const val USER = "sa"
    private const val PASSWORD = ""

    init {
        initializeDatabase()
        seedData()
    }

    private fun getConnection() = DriverManager.getConnection(URL, USER, PASSWORD)

    private fun initializeDatabase() {
        getConnection().use { connection ->
            // Читаем SQL из файлов ресурсов
            val createTablesSql = readSqlFile("init/create_tables.sql")

            connection.createStatement().use { statement ->
                // Выполняем все SQL команды из файла
                createTablesSql.split(";")
                    .filter { it.isNotBlank() }
                    .forEach { sql ->
                        if (sql.trim().isNotBlank()) {
                            statement.execute(sql.trim())
                        }
                    }
            }
        }
    }

    private fun seedData() {
        getConnection().use { connection ->
            // Проверяем, есть ли уже данные
            val checkUsers = "SELECT COUNT(*) as count FROM users"
            val resultSet = connection.createStatement().executeQuery(checkUsers)
            resultSet.next()
            val userCount = resultSet.getInt("count")

            if (userCount == 0) {
                // Выполняем SQL из insert_data.sql файла
                executeInsertDataSql(connection)
            }
        }
    }

    private fun executeInsertDataSql(connection: java.sql.Connection) {
        val insertDataSql = readSqlFile("init/insert_data.sql")

        connection.createStatement().use { statement ->
            // Выполняем все SQL команды из файла
            insertDataSql.split(";")
                .filter { it.isNotBlank() }
                .forEach { sql ->
                    if (sql.trim().isNotBlank()) {
                        statement.execute(sql.trim())
                    }
                }
        }
    }

    private fun readSqlFile(filePath: String): String {
        return this::class.java.classLoader
            .getResource("sql/$filePath")
            ?.readText()
            ?: throw IllegalArgumentException("SQL file not found: $filePath")
    }
}