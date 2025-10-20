package services

import database.Resource

class ResourceRepository(private val resources: List<Resource>) {
    fun findByPath(path: String): Resource? =
        resources.firstOrNull { it.path == path }
}