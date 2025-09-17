fun main(args: Array<String>) {
    val words = if (args.isNotEmpty()) {
        args.toList()
    } else {
        return
    }

    val frequencyArr = words.sorted()
        .groupingBy { it }.eachCount()

    frequencyArr.entries
        .sortedWith(compareByDescending<Map.Entry<String, Int>> { it.value }
            .thenBy { it.key })
        .forEach { println("${it.key} ${it.value}") }
}