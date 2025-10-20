package services

import infrastructure.persistence.mock.MockDatabase
import enums.Role
import exceptions.*

class ResourceAuthorizer (
    private val authService: AuthenticationService,
    private val resourceRepo: ResourceRepository,
    private val authorizer: AuthorizationService
) {

    fun authorize(login: String, password: String, resourcePath: String, role: Role, volume: Int) {
        val user = authService.authenticate(login, password)
            ?: if (authService.findUser(login) == null) throw InvalidLoginException()
            else throw InvalidPasswordException()

        val resource = resourceRepo.findByPath(resourcePath)
            ?: throw ResourceNotFoundException()

        if (!authorizer.hasAccess(login, resourcePath, role))
            throw AccessDeniedException()

        if (volume > resource.maxVolume)
            throw ExceededVolumeException()
    }
}