package lab7.infrastructure.persistence.mock

import application.port.out.UserRepository
import database.User
import infrastructure.persistence.mock.MockDatabase
import org.springframework.stereotype.Repository

@Repository
open class MockUserRepositoryAdapter : UserRepository {
    override fun findByLogin(login: String): User? {
        return MockDatabase.users.find { it.login == login }
    }

    override fun findAll(): List<User> {
        return MockDatabase.users
    }
}