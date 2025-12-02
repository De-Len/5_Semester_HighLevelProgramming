package application.port.out

import database.User

interface UserRepository {
    fun findByLogin(login: String): User?
    fun findAll(): List<User>
}