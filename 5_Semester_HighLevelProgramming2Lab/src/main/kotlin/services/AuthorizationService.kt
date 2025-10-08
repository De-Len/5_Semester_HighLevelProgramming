package services

import database.Permission
import database.Resource
import enums.Role

class AuthorizationService(
    private val permissions: List<Permission>
) {
    fun hasAccess(login: String, resourcePath: String, role: Role): Boolean {
        val subPaths = ResourceParser.generateSubPaths(resourcePath).reversed()

        for (currentPath in subPaths) {
            val hasPermission = permissions.any {
                it.userLogin == login &&
                        it.resourcePath == currentPath &&
                        it.role == role
            }
            if (hasPermission) return true
        }
        return false
    }
}