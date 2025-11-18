package application.port.out

import database.Resource

interface ResourceRepository {
    fun findByPath(path: String): Resource?
    fun findAll(): List<Resource>
}