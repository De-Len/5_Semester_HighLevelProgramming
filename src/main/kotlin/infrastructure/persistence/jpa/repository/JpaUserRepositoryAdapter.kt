package lab7.infrastructure.persistence.jpa.repository

import application.port.out.UserRepository
import database.User
import lab7.infrastructure.persistence.jpa.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface UserJpaRepository : JpaRepository<UserEntity, String>

@Repository
open class JpaUserRepositoryAdapter(
    private val repository: UserJpaRepository
) : UserRepository {

    override fun findByLogin(login: String): User? {
        val entity = repository.findById(login).orElse(null) ?: return null
        return entity.toDomain()
    }

    override fun findAll(): List<User> {
        return repository.findAll().map { it.toDomain() }
    }

    // Расширение для конвертации Entity → Domain
    private fun UserEntity.toDomain(): User {
        return User(
            login = this.login,
            passwordHash = this.passwordHash,
            salt = this.salt
        )
    }
}