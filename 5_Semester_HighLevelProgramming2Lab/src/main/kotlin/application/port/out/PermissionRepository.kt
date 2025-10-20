package application.port.out

import database.Permission

interface PermissionRepository {
    fun findByUserAndResource(login: String, resourcePath: String): List<Permission>
    fun findAll(): List<Permission>
}