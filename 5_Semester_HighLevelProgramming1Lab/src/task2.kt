fun main(args: Array<String>) {
    val words = if (args.isNotEmpty()) {
        args.toList()
    } else {
        return
    }

    words.sorted().forEach { println(it) }
}