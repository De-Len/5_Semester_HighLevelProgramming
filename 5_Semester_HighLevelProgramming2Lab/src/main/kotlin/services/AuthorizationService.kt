package services

import database.Permission
import database.Resource
import enums.Role

class AuthorizationService(
    private val permissions: List<Permission>,
    private val resources: List<Resource>
) {
    fun findResource(path: String): Resource? = resources.firstOrNull { it.path == path }

    fun hasAccess(login: String, resourcePath: String, role: Role): Boolean {
        val pathSegments = resourcePath.split('.')
        for (i in pathSegments.size downTo 1) {
            val currentPath = pathSegments.subList(0, i).joinToString(".")
            val hasPermission = permissions.any {
                it.userLogin == login && it.resourcePath == currentPath && it.role == role
            }
            if (hasPermission) {
                return true
            }
        }
        return false
    }
}
