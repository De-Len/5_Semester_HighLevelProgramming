package application.port.out

import database.Permission
import enums.Role

interface PermissionRepository {
    fun findByUserAndResource(login: String, resourcePath: String): List<Permission>
    fun findAll(): List<Permission>
    fun hasAccess(login: String, resourcePath: String, role: Role): Boolean
}