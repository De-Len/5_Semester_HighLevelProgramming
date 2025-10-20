// src/main/kotlin/Main.kt

import enums.ExitCode
import enums.Role
import exceptions.ExceededVolumeException
import exceptions.InvalidLoginException
import exceptions.InvalidPasswordException
import exceptions.ResourceNotFoundException
import infrastructure.persistence.mock.MockPermissionRepositoryAdapter
import infrastructure.persistence.mock.MockResourceRepositoryAdapter
import infrastructure.persistence.mock.MockUserRepositoryAdapter
import kotlinx.cli.*
import services.AuthenticationService
import services.AuthorizationService
import services.ResourceAuthorizer
import services.ServiceResourceRepository
import kotlin.system.exitProcess


/** код сгенерирован Manus **/


fun main(args: Array<String>) {
    val parser = ArgParser("resource-authorizer", strictSubcommandOptionsOrder = true)

    val login by parser.option(ArgType.String, shortName = "login").required()
    val password by parser.option(ArgType.String, shortName = "pass").required()
    val resourcePath by parser.option(ArgType.String, shortName = "res").required()
    val role by parser.option(ArgType.Choice<Role>(), shortName = "role").required()
    val volume by parser.option(ArgType.Int, shortName = "vol").required()

    try {
        parser.parse(args)
    } catch (e: Exception) {
        exitProcess(ExitCode.UNKNOWN_ACTION.code)
    }

    val mockUserRepository = MockUserRepositoryAdapter()
    val mockResourceRepository = MockResourceRepositoryAdapter()
    val mockPermissionRepository = MockPermissionRepositoryAdapter()

    val authService = AuthenticationService(mockUserRepository)
    val serviceResourceRepository = ServiceResourceRepository(mockResourceRepository)
    val authorizationService = AuthorizationService(mockPermissionRepository)

    val authorizer = ResourceAuthorizer(authService, serviceResourceRepository, authorizationService)

    try {
        authorizer.authorize(login, password, resourcePath, role, volume)
        exitProcess(ExitCode.SUCCESS.code)
    } catch (e: InvalidLoginException) {
        exitProcess(ExitCode.INVALID_LOGIN.code)
    } catch (e: InvalidPasswordException) {
        exitProcess(ExitCode.INVALID_PASSWORD.code)
    } catch (e: ResourceNotFoundException) {
        exitProcess(ExitCode.RESOURCE_NOT_FOUND.code)
    } catch (e: AccessDeniedException) {
        exitProcess(ExitCode.ACCESS_DENIED.code)
    } catch (e: ExceededVolumeException) {
        exitProcess(ExitCode.EXCEEDED_MAX_VOLUME.code)
    } catch (e: Exception) {
        exitProcess(ExitCode.UNKNOWN_ACTION.code)
    }
}