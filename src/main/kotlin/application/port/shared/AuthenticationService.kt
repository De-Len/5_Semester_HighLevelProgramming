package lab7.application.port.shared

import application.port.out.UserRepository
import database.User
import infrastructure.services.HashingService
import org.springframework.stereotype.Service

@Service
open class AuthenticationService(private val userRepository: UserRepository) {
    fun findUser(login: String): User? = userRepository.findByLogin(login)

    fun authenticate(login: String, pass: String): User? {
        val user = findUser(login) ?: return null
        val salt = HashingService.saltFromBase64(user.salt)
        val hash = HashingService.hashPassword(pass, salt)
        return if (hash == user.passwordHash) user else null
    }
}