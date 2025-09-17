fun main(args: Array<String>) {
    val words = if (args.isNotEmpty()) {
        args.toList()
    } else {
        return
    }

    words.forEach { println(it) }
}