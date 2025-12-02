package infrastructure.services

object ResourceParser {
    fun splitPath(path: String): List<String> =
        path.split('.').filter { it.isNotBlank() }

    fun generateSubPaths(path: String): List<String> {
        val parts = splitPath(path)
        return (1..parts.size).map { i ->
            parts.take(i).joinToString(".")
        }
    }
}