fun main(args: Array<String>) {
    val words = if (args.isNotEmpty()) {
        args.toList()
    } else {
        return
    }

    val frequencyArr = words.sorted()
        .groupingBy { it }.eachCount()
    frequencyArr.forEach { println("${it.key} ${it.value}") }
}