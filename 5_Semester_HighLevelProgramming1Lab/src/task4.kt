fun main() {
    println("Введите строку:")
    val text = readLine()
    if (text != null) {
        val stringArr = text.split(" ").sorted()
        val frequency = stringArr.groupingBy { it }.eachCount()
        for (string in frequency) {
            println(string.key + " " + string.value)

        }
    }
}