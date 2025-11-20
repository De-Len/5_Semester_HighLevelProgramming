package infrastructure.persistence.database

import application.port.out.PermissionRepository
import database.Permission
import domain.enums.Role
import infrastructure.services.ResourceParser
import java.sql.DriverManager

class H2PermissionRepositoryAdapter : PermissionRepository {
    private val url = "jdbc:h2:./database/authdb"
    private val user = "sa"
    private val password = ""

    init {
        // Инициализируем базу при создании репозитория
        DatabaseInitializer
    }

    private fun getConnection() = DriverManager.getConnection(url, user, password)

    override fun findByUserAndResource(login: String, resourcePath: String): List<Permission> {
        val sql = """
            SELECT user_login, resource_path, role 
            FROM permissions 
            WHERE user_login = ? AND resource_path = ?
        """.trimIndent()

        return getConnection().use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, login)
                statement.setString(2, resourcePath)
                val resultSet = statement.executeQuery()

                val permissions = mutableListOf<Permission>()
                while (resultSet.next()) {
                    permissions.add(
                        Permission(
                            userLogin = resultSet.getString("user_login"),
                            resourcePath = resultSet.getString("resource_path"),
                            role = Role.valueOf(resultSet.getString("role"))
                        )
                    )
                }
                permissions
            }
        }
    }

    override fun findAll(): List<Permission> {
        val sql = """
            SELECT user_login, resource_path, role 
            FROM permissions
        """.trimIndent()

        return getConnection().use { connection ->
            connection.createStatement().use { statement ->
                val resultSet = statement.executeQuery(sql)

                val permissions = mutableListOf<Permission>()
                while (resultSet.next()) {
                    permissions.add(
                        Permission(
                            userLogin = resultSet.getString("user_login"),
                            resourcePath = resultSet.getString("resource_path"),
                            role = Role.valueOf(resultSet.getString("role"))
                        )
                    )
                }
                permissions
            }
        }
    }

    override fun hasAccess(login: String, resourcePath: String, role: Role): Boolean {
        val subPaths = ResourceParser.generateSubPaths(resourcePath).reversed()

        for (currentPath in subPaths) {
            val sql = """
                SELECT COUNT(*) as count 
                FROM permissions 
                WHERE user_login = ? AND resource_path = ? AND role = ?
            """.trimIndent()

            val hasPermission = getConnection().use { connection ->
                connection.prepareStatement(sql).use { statement ->
                    statement.setString(1, login)
                    statement.setString(2, currentPath)
                    statement.setString(3, role.toString())
                    val resultSet = statement.executeQuery()
                    resultSet.next()
                    resultSet.getInt("count") > 0
                }
            }
            if (hasPermission) return true
        }
        return false
    }
}