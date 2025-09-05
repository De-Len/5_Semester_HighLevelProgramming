import java.util.*
fun readWordsFromStdin(): List<String> {
    val scanner = Scanner(System.`in`)
    val words = mutableListOf<String>()

    while (scanner.hasNext()) {
        words.add(scanner.next())
    }

    return words
}

fun main(text: Array<String>) {
    val words = if (text.isNotEmpty()) {
        text.toList()
    } else {
        readWordsFromStdin()
    }
    val frequency = words.groupingBy { it }.eachCount()

    val maxCount = frequency.maxBy { it.value }.value

    if (maxCount > 1) {
        val sortedFrequency = frequency.entries.sortedByDescending { it.value }

        for (string in sortedFrequency) {
            println(string.key + " " + string.value)

        }
    }
    else {
        for (string in frequency) {
            println(string.key + " " + string.value)

        }
    }
}