package infrastructure.persistence.mock

import application.port.out.PermissionRepository
import database.Permission
import enums.Role
import services.ResourceParser

class MockPermissionRepositoryAdapter : PermissionRepository {
    override fun findByUserAndResource(login: String, resourcePath: String): List<Permission> {
        return MockDatabase.permissions.filter { it.userLogin == login && it.resourcePath == resourcePath }
    }

    override fun findAll(): List<Permission> {
        return MockDatabase.permissions
    }
    override fun hasAccess(login: String, resourcePath: String, role: Role): Boolean {
        val subPaths = ResourceParser.generateSubPaths(resourcePath).reversed()

        for (currentPath in subPaths) {
            val hasPermission = MockDatabase.permissions.any {
                it.userLogin == login &&
                        it.resourcePath == currentPath &&
                        it.role == role
            }
            if (hasPermission) return true
        }
        return false
    }
}