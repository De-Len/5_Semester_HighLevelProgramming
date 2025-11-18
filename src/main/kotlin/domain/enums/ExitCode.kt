package domain.enums

enum class ExitCode(val code: Int) {
    SUCCESS(0),                    // Успешное выполнение
    HELP(1),                       // Запрошена справка
    INVALID_PASSWORD(2),           // Неверный пароль
    INVALID_LOGIN(3),              // Неверный логин
    UNKNOWN_ACTION(4),             // Неизвестное действие над ресурсом
    ACCESS_DENIED(5),              // Нет доступа
    RESOURCE_NOT_FOUND(6),         // Несуществующий ресурс
    INVALID_FORMAT(7),             // Некорректный формат ресурса или объема
    EXCEEDED_MAX_VOLUME(8);        // Превышение максимального объема
}