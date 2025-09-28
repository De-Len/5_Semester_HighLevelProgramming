// src/main/kotlin/Main.kt

import kotlinx.cli.*
import java.security.MessageDigest
import java.security.SecureRandom
import kotlin.system.exitProcess


enum class Role {
    READ, WRITE, EXECUTE
}

data class Permission(val userLogin: String, val resourcePath: String, val role: Role)

data class User(val login: String, val passHash: String, val salt: String)

data class Resource(val path: String, val maxVolume: Int)

enum class ExitCode(val code: Int) {
    SUCCESS(0),
    HELP(0), // Вывод справки также считается успешным завершением
    INVALID_ARGUMENTS(1),
    UNKNOWN_USER(2),
    INVALID_PASSWORD(3),
    FORBIDDEN(4),
    INSUFFICIENT_VOLUME(5)
}

/** код сгенерирован Manus **/
object HashingService {
    private const val HASH_ALGORITHM = "SHA-256"

    /** Генерирует новую соль */
    fun generateSalt(): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return salt
    }

    /**
     * Хэширует пароль с использованием соли.
     * @param password Пароль в виде строки.
     * @param salt Соль в виде массива байт.
     * @return Хэш в виде строки (Base64).
     */
    fun hashPassword(password: String, salt: ByteArray): String {
        val md = MessageDigest.getInstance(HASH_ALGORITHM)
        md.update(salt)
        val hashedPassword = md.digest(password.toByteArray(Charsets.UTF_8))
        return java.util.Base64.getEncoder().encodeToString(hashedPassword)
    }

    /** Удобная функция для преобразования строки Base64 в ByteArray */
    fun saltFromBase64(saltString: String): ByteArray = java.util.Base64.getDecoder().decode(saltString)

    /** Удобная функция для преобразования ByteArray в строку Base64 */
    fun saltToBase64(salt: ByteArray): String = java.util.Base64.getEncoder().encodeToString(salt)
}

class AuthenticationService(private val users: List<User>) {
    fun findUser(login: String): User? = users.firstOrNull { it.login == login }

    fun authenticate(login: String, pass: String): User? {
        val user = findUser(login) ?: return null
        val salt = HashingService.saltFromBase64(user.salt)
        val hash = HashingService.hashPassword(pass, salt)
        return if (hash == user.passHash) user else null
    }
}

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


object MockDatabase {
    val users: List<User>
    val resources: List<Resource>
    val permissions: List<Permission>

    init {
        val user1Salt = HashingService.generateSalt()
        val user2Salt = HashingService.generateSalt()

        users = listOf(
            User("user1", HashingService.hashPassword("pass1", user1Salt), HashingService.saltToBase64(user1Salt)),
            User("user2", HashingService.hashPassword("pass2", user2Salt), HashingService.saltToBase64(user2Salt))
        )

        resources = listOf(
            Resource("A", 100),
            Resource("A.B", 50),
            Resource("A.B.C", 20),
            Resource("D.E", 10)
        )

        permissions = listOf(
            Permission("user1", "A", Role.READ),
            Permission("user1", "A.B", Role.WRITE),
            Permission("user2", "A.B.C", Role.EXECUTE)
        )
    }
}

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
