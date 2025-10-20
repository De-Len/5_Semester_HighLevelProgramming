package infrastructure.persistence.mock

import application.port.out.UserRepository
import database.User

class MockUserRepositoryAdapter : UserRepository {
    override fun findByLogin(login: String): User? {
        return MockDatabase.users.find { it.login == login }
    }

    override fun findAll(): List<User> {
        return MockDatabase.users
    }
}