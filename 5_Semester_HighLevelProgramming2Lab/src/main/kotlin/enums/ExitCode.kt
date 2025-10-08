package enums

enum class ExitCode(val code: Int) {
    SUCCESS(0),
    HELP(0), // Вывод справки также считается успешным завершением
    INVALID_ARGUMENTS(1),
    UNKNOWN_USER(2),
    INVALID_PASSWORD(3),
    FORBIDDEN(4),
    INSUFFICIENT_VOLUME(5)
}
