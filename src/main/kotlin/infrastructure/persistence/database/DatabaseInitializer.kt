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
                // Создаем тестовых пользователей
                val user1Salt = HashingService.generateSalt()
                val user2Salt = HashingService.generateSalt()

                // Добавляем пользователей
                insertUser(connection, User("user1", HashingService.hashPassword("pass1", user1Salt), HashingService.saltToBase64(user1Salt)))
                insertUser(connection, User("user2", HashingService.hashPassword("pass2", user2Salt), HashingService.saltToBase64(user2Salt)))

                // Добавляем ресурсы
                insertResource(connection, Resource("A", 100))
                insertResource(connection, Resource("A.B", 50))
                insertResource(connection, Resource("A.B.C", 20))
                insertResource(connection, Resource("D.E", 10))

                // Добавляем разрешения
                insertPermission(connection, Permission("user1", "A", Role.READ))
                insertPermission(connection, Permission("user1", "A.B", Role.WRITE))
                insertPermission(connection, Permission("user2", "A.B.C", Role.EXECUTE))
            }
        }
    }

    private fun insertUser(connection: java.sql.Connection, user: User) {
        val sql = "INSERT INTO users (login, password_hash, salt) VALUES (?, ?, ?)"
        connection.prepareStatement(sql).use { statement ->
            statement.setString(1, user.login)
            statement.setString(2, user.passHash)
            statement.setString(3, user.salt)
            statement.executeUpdate()
        }
    }

    private fun insertResource(connection: java.sql.Connection, resource: Resource) {
        val sql = "INSERT INTO resources (path, volume) VALUES (?, ?)"
        connection.prepareStatement(sql).use { statement ->
            statement.setString(1, resource.path)
            statement.setInt(2, resource.maxVolume)
            statement.executeUpdate()
        }
    }

    private fun insertPermission(connection: java.sql.Connection, permission: Permission) {
        val sql = "INSERT INTO permissions (user_login, resource_path, role) VALUES (?, ?, ?)"
        connection.prepareStatement(sql).use { statement ->
            statement.setString(1, permission.userLogin)
            statement.setString(2, permission.resourcePath)
            statement.setString(3, permission.role.toString())
            statement.executeUpdate()
        }
    }

    private fun readSqlFile(filePath: String): String {
        return this::class.java.classLoader
            .getResource("sql/$filePath")
            ?.readText()
            ?: throw IllegalArgumentException("SQL file not found: $filePath")
    }
}