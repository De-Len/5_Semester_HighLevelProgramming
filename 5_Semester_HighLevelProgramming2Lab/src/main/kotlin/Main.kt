// src/main/kotlin/Main.kt

import database.MockDatabase
import enums.ExitCode
import enums.Role
import kotlinx.cli.*
import services.AuthenticationService
import services.AuthorizationService
import kotlin.system.exitProcess


/** код сгенерирован Manus **/


fun main(args: Array<String>) {
    val parser = ArgParser("resource-authorizer", strictSubcommandOptionsOrder = true)

    val login by parser.option(ArgType.String, shortName = "login", description = "Логин пользователя").required()
    val password by parser.option(ArgType.String, shortName = "pass", description = "Пароль").required()
    val resourcePath by parser.option(ArgType.String, shortName = "res", description = "Путь к ресурсу (например, A.B.C)").required()

    val role by parser.option(ArgType.Choice<Role>(), shortName = "role", description = "Запрашиваемое действие").required()
    val volume by parser.option(ArgType.Int, shortName = "vol", description = "Объем запрашиваемого ресурса").required()

    try {
        parser.parse(args)
    } catch (e: Exception) {
        // kotlinx-cli автоматически печатает справку при ошибке парсинга
        exitProcess(ExitCode.INVALID_ARGUMENTS.code)
    }

    val authService = AuthenticationService(MockDatabase.users)
    val authorizer = AuthorizationService(MockDatabase.permissions, MockDatabase.resources)

    val user = authService.authenticate(login, password)
    if (user == null) {
        if (authService.findUser(login) == null) {
            exitProcess(ExitCode.UNKNOWN_USER.code)
        } else {
            exitProcess(ExitCode.INVALID_PASSWORD.code)
        }
    }

    val resource = authorizer.findResource(resourcePath)
    if (resource == null) {
        exitProcess(ExitCode.FORBIDDEN.code)
    }

    if (!authorizer.hasAccess(login, resourcePath, role)) {
        exitProcess(ExitCode.FORBIDDEN.code)
    }

    if (volume > resource.maxVolume) {
        exitProcess(ExitCode.INSUFFICIENT_VOLUME.code)
    }

    exitProcess(ExitCode.SUCCESS.code)
}
