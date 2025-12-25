package lab7.usecase

import domain.enums.ExitCode
import domain.enums.Role
import domain.exceptions.*
import kotlinx.cli.*
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import lab7.usecase.ResourceAuthorizer
import kotlin.system.exitProcess

@Component
open class AuthorizerRunner(
    private val authorizer: ResourceAuthorizer
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun run(vararg args: String) {
        val parser = ArgParser("resource-authorizer", strictSubcommandOptionsOrder = true)

        val login by parser.option(ArgType.String, shortName = "login").required()
        val password by parser.option(ArgType.String, shortName = "pass").required()
        val resourcePath by parser.option(ArgType.String, shortName = "res").required()
        val role by parser.option(ArgType.Choice<Role>(), shortName = "role").required()
        val volume by parser.option(ArgType.Int, shortName = "vol").required()

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
}