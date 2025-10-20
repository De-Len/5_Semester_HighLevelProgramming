package services

import application.port.out.PermissionRepository
import database.Permission
import database.Resource
import enums.Role

class AuthorizationService(
    private val permissionRepository: PermissionRepository
) {
    fun hasAccess(login: String, resourcePath: String, role: Role): Boolean {
        return permissionRepository.hasAccess(login, resourcePath, role)
    }
}