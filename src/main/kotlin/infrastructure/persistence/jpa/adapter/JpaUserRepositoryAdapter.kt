package lab7.infrastructure.persistence.jpa.adapter

import application.port.out.PermissionRepository
import domain.enums.Role
import database.Permission
import lab7.infrastructure.persistence.jpa.repository.JpaPermissionRepository
import lab7.infrastructure.persistence.jpa.repository.JpaUserRepository
import infrastructure.services.ResourceParser
import org.springframework.stereotype.Repository


@Repository
class JpaPermissionRepositoryAdapter(
    private val permissionRepo: JpaPermissionRepository,
    private val userRepo: JpaUserRepository
) : PermissionRepository {

    override fun findByUserAndResource(
        login: String,
        resourcePath: String
    ): List<Permission> {

        val user = userRepo.findByLogin(login) ?: return emptyList()

        return permissionRepo
            .findAllByUser_Id(user.id)
            .filter { it.resource.path == resourcePath }
            .map { it.toDomain() }
    }

    override fun findAll(): List<Permission> =
        permissionRepo.findAll().map { it.toDomain() }

    override fun hasAccess(
        login: String,
        resourcePath: String,
        role: Role
    ): Boolean {

        val user = userRepo.findByLogin(login) ?: return false

        val permissions = permissionRepo
            .findAllByUser_Id(user.id)

        val subPaths = ResourceParser
            .generateSubPaths(resourcePath)
            .reversed()

        return subPaths.any { path ->
            permissions.any {
                it.resource.path == path && it.role == role
            }
        }
    }
}