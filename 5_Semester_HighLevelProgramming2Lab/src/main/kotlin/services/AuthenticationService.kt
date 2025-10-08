package services

import database.User

class AuthenticationService(private val users: List<User>) {
    fun findUser(login: String): User? = users.firstOrNull { it.login == login }

    fun authenticate(login: String, pass: String): User? {
        val user = findUser(login) ?: return null
        val salt = HashingService.saltFromBase64(user.salt)
        val hash = HashingService.hashPassword(pass, salt)
        return if (hash == user.passHash) user else null
    }
}