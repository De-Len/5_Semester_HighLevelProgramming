package lab7.infrastructure.persistence.jpa.repository

import application.port.out.PermissionRepository
import database.Permission
import domain.enums.Role
import infrastructure.services.ResourceParser.generateSubPaths
import lab7.infrastructure.persistence.jpa.entity.PermissionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface PermissionJpaRepository : JpaRepository<PermissionEntity, Long> {
    fun findAllByUserLoginAndResourcePath(userLogin: String, resourcePath: String): List<PermissionEntity>
    fun findAllByUserLoginAndResourcePathAndRole(userLogin: String, resourcePath: String, role: Role): List<PermissionEntity>
}

@Repository
open class JpaPermissionRepositoryAdapter(
    private val repository: PermissionJpaRepository
) : PermissionRepository {

    override fun findByUserAndResource(login: String, resourcePath: String) =
        repository.findAllByUserLoginAndResourcePath(login, resourcePath).map { it.toDomain() }

    override fun findAll() = repository.findAll().map { it.toDomain() }

    override fun hasAccess(login: String, resourcePath: String, role: Role): Boolean {
        val subPaths = generateSubPaths(resourcePath).reversed()

        for (currentPath in subPaths) {
            val hasPermission = repository.findAllByUserLoginAndResourcePathAndRole(login, currentPath, role).isNotEmpty()
            if (hasPermission) return true
        }
        return false
    }

    private fun PermissionEntity.toDomain(): Permission {
        return Permission(
            userLogin = this.userLogin,
            resourcePath = this.resourcePath,
            role = this.role
        )
    }
}