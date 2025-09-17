fun main(text: Array<String>) {
    val words = if (text.isNotEmpty()) {
        text.toList()
    } else {
        readln().split(" ")
    }

    val frequencyArr = words.sorted()
        .groupingBy { it }.eachCount()

    frequencyArr.entries
        .sortedWith(compareByDescending<Map.Entry<String, Int>> { it.value }
            .thenBy { it.key })
        .forEach { println("${it.key} ${it.value}") }
}