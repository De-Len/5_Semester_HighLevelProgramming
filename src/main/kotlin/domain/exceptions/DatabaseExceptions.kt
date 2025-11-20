package domain.exceptions

import domain.enums.ExitCode

sealed class DatabaseException(
    val exitCode: ExitCode,
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)

class DatabaseConnectionException(
    message: String = "Не удалось подключиться к базе данных",
    cause: Throwable? = null
) : DatabaseException(ExitCode.DATABASE_CONNECTION_ERROR, message, cause)

class SqlQueryException(
    message: String = "Ошибка выполнения SQL-запроса",
    cause: Throwable? = null
) : DatabaseException(ExitCode.SQL_QUERY_ERROR, message, cause)