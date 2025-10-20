package usecase

import application.port.shared.AuthenticationService
import application.port.shared.AuthorizationService
import application.port.shared.ServiceResourceRepository
import domain.enums.Role
import domain.exceptions.*

class ResourceAuthorizer (
    private val authService: AuthenticationService,
    private val resourceRepo: ServiceResourceRepository,
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