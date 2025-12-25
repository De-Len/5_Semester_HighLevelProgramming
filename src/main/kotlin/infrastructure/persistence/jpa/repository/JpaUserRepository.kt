package lab7.infrastructure.persistence.jpa.repository

import lab7.infrastructure.persistence.jpa.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaUserRepository : JpaRepository<UserEntity, Long> {
    fun findByLogin(login: String): UserEntity?
}