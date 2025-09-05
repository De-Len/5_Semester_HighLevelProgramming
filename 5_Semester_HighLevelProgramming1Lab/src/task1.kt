fun main() {
    println("Введите строку:")
    val text = readLine()
    if (text != null) {
        val stringArr = text.split(" ")

        for (string in stringArr) {
            println(string)
        }
    }
}