package lab7.usecase

import lab7.application.port.shared.AuthenticationService
import lab7.application.port.shared.AuthorizationService
import lab7.application.port.shared.ServiceResourceRepository
import domain.enums.Role
import domain.exceptions.*
import org.springframework.stereotype.Component

@Component
open class ResourceAuthorizer (
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