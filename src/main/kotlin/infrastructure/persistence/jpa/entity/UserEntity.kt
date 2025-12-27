package lab7.infrastructure.persistence.jpa.entity

import database.User
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    val login: String,

    @Column(nullable = false)
    val passwordHash: String,

    @Column(nullable = false)
    val salt: String
)