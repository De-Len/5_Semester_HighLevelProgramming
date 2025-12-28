package lab7.usecase

import domain.enums.ExitCode
import domain.enums.Role
import domain.exceptions.*
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import kotlin.system.exitProcess

@Component
open class AuthorizerRunner(
    private val authorizer: ResourceAuthorizer
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun run(vararg args: String) {
        val parser = ArgParser("resource-authorizer", strictSubcommandOptionsOrder = true)

        val login by parser.option(ArgType.String, fullName = "login", shortName = "l").required()
        val password by parser.option(ArgType.String, fullName = "password", shortName = "p").required()
        val resourcePath by parser.option(ArgType.String, fullName = "resourcePath", shortName = "r").required()
        val role by parser.option(ArgType.Choice<Role>(), fullName = "role", shortName = "o").required()
        val volume by parser.option(ArgType.Int, fullName = "volume", shortName = "v").required()

        parser.parse(args)

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