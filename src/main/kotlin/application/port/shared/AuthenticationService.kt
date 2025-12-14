package application.port.shared

import application.port.out.UserRepository
import database.User
import infrastructure.services.HashingService
import org.springframework.stereotype.Service

@Service
class AuthenticationService(private val userRepository: UserRepository) {
    fun findUser(login: String): User? = userRepository.findByLogin(login)

    fun authenticate(login: String, pass: String): User? {
        val user = findUser(login) ?: return null
        val salt = HashingService.saltFromBase64(user.salt)
        val hash = HashingService.hashPassword(pass, salt)
        return if (hash == user.passHash) user else null
    }
}