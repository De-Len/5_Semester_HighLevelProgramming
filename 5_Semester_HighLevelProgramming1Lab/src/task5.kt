import kotlin.math.max

fun main() {
    println("Введите строку:")
    val text = readLine()
    if (text != null) {
        val stringArr = text.split(" ").sorted()
        val frequency = stringArr.groupingBy { it }.eachCount()

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
}