package infrastructure.persistence.database

import application.port.out.UserRepository
import database.User
import java.sql.DriverManager

class H2UserRepositoryAdapter : UserRepository {
    private val url = "jdbc:h2:./database/authdb"
    private val user = "sa"
    private val password = ""

    init {
        DatabaseInitializer
    }

    private fun getConnection() = DriverManager.getConnection(url, user, password)

    override fun findByLogin(login: String): User? {
        val sql = """
            SELECT login, password_hash, salt 
            FROM users 
            WHERE login = ?
        """.trimIndent()

        return getConnection().use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, login)
                val resultSet = statement.executeQuery()

                if (resultSet.next()) {
                    User(
                        login = resultSet.getString("login"),
                        passHash = resultSet.getString("password_hash"),
                        salt = resultSet.getString("salt")
                    )
                } else {
                    null
                }
            }
        }
    }

    override fun findAll(): List<User> {
        val sql = """
            SELECT login, password_hash, salt 
            FROM users
        """.trimIndent()

        return getConnection().use { connection ->
            connection.createStatement().use { statement ->
                val resultSet = statement.executeQuery(sql)

                val users = mutableListOf<User>()
                while (resultSet.next()) {
                    users.add(
                        User(
                            login = resultSet.getString("login"),
                            passHash = resultSet.getString("password_hash"),
                            salt = resultSet.getString("salt")
                        )
                    )
                }
                users
            }
        }
    }
}