package infrastructure.persistence.mock

import application.port.out.PermissionRepository
import database.Permission

class MockPermissionRepositoryAdapter : PermissionRepository {
    override fun findByUserAndResource(login: String, resourcePath: String): List<Permission> {
        return MockDatabase.permissions.filter { it.userLogin == login && it.resourcePath == resourcePath }
    }

    override fun findAll(): List<Permission> {
        return MockDatabase.permissions
    }
}