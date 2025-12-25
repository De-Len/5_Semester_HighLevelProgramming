package infrastructure.persistence.jpa.adapter

import application.port.out.PermissionRepository
import database.Permission
import domain.enums.Role
import infrastructure.services.ResourceParser
import lab7.infrastructure.persistence.jpa.repository.JpaPermissionRepository
import org.springframework.stereotype.Repository

@Repository
class JpaPermissionRepositoryAdapter(
    private val jpaRepo: JpaPermissionRepository
) : PermissionRepository {

    override fun findByUserAndResource(
        login: String,
        resourcePath: String
    ): List<Permission> =
        jpaRepo
            .findAllByUserLoginAndResourcePath(login, resourcePath)
            .map { it.toDomain() }

    override fun findAll(): List<Permission> =
        jpaRepo.findAll().map { it.toDomain() }

    override fun hasAccess(
        login: String,
        resourcePath: String,
        role: Role
    ): Boolean {
        val subPaths = ResourceParser.generateSubPaths(resourcePath).reversed()

        return subPaths.any { path ->
            jpaRepo.existsByUserLoginAndResourcePathAndRole(
                login, path, role
            )
        }
    }
}