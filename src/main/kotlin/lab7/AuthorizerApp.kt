import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class AuthorizerApp

fun main(args: Array<String>) {
    runApplication<AuthorizerApp>(*args)
}