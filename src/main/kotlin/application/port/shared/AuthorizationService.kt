package application.port.shared

import application.port.out.PermissionRepository
import domain.enums.Role

class AuthorizationService(
    private val permissionRepository: PermissionRepository
) {
    fun hasAccess(login: String, resourcePath: String, role: Role): Boolean {
        return permissionRepository.hasAccess(login, resourcePath, role)
    }
}