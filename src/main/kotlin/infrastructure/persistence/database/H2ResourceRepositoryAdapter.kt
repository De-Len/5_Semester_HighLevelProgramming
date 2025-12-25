package infrastructure.persistence.database

import application.port.out.ResourceRepository
import database.Resource
import java.sql.DriverManager

class H2ResourceRepositoryAdapter : ResourceRepository {
    private val url = "jdbc:h2:./database/authdb"
    private val user = "sa"
    private val password = ""

    init {
        DatabaseInitializer
    }

    private fun getConnection() = DriverManager.getConnection(url, user, password)

    override fun findByPath(path: String): Resource? {
        val sql = """
            SELECT path, volume 
            FROM resources 
            WHERE path = ?
        """.trimIndent()

        return getConnection().use { connection ->
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, path)
                val resultSet = statement.executeQuery()

                if (resultSet.next()) {
                    Resource(
                        path = resultSet.getString("path"),
                        maxVolume = resultSet.getInt("volume")
                    )
                } else {
                    null
                }
            }
        }
    }

    override fun findAll(): List<Resource> {
        val sql = """
            SELECT path, volume 
            FROM resources
        """.trimIndent()

        return getConnection().use { connection ->
            connection.createStatement().use { statement ->
                val resultSet = statement.executeQuery(sql)

                val resources = mutableListOf<Resource>()
                while (resultSet.next()) {
                    resources.add(
                        Resource(
                            path = resultSet.getString("path"),
                            maxVolume = resultSet.getInt("volume")
                        )
                    )
                }
                resources
            }
        }
    }
}