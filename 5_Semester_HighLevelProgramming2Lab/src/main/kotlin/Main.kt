// src/main/kotlin/Main.kt

import database.MockDatabase
import enums.ExitCode
import enums.Role
import kotlinx.cli.*
import services.AuthenticationService
import services.AuthorizationService
import services.ResourceRepository
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
        exitProcess(ExitCode.UNKNOWN_ACTION.code)
    }

    val authService = AuthenticationService(MockDatabase.users)
    val resourceRepo = ResourceRepository(MockDatabase.resources)
    val authorizer = AuthorizationService(MockDatabase.permissions)

    val user = authService.authenticate(login, password)
    if (user == null) {
        if (authService.findUser(login) == null) {
            exitProcess(ExitCode.INVALID_LOGIN.code)
        } else {
            exitProcess(ExitCode.INVALID_PASSWORD.code)
        }
    }

    val resource = resourceRepo.findByPath(resourcePath)
    if (resource == null) {
        exitProcess(ExitCode.RESOURCE_NOT_FOUND.code)
    }

    if (!authorizer.hasAccess(login, resourcePath, role)) {
        exitProcess(ExitCode.ACCESS_DENIED.code)
    }

    if (volume > resource.maxVolume) {
        exitProcess(ExitCode.EXCEEDED_MAX_VOLUME.code)
    }

    // если всё ок
    exitProcess(ExitCode.SUCCESS.code)
}
