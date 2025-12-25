package lab7.infrastructure.persistence.jpa.entity

import database.User
import jakarta.persistence.*

@Entity
@Table(
    name = "users",
    uniqueConstraints = [UniqueConstraint(columnNames = ["login"])]
)
class UserEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 50)
    val login: String,

    @Column(nullable = false)
    val passwordHash: String,

    @Column(nullable = false)
    val salt: String
)

fun UserEntity.toDomain(): User =
    User(
        login = login,
        passHash = passwordHash,
        salt = salt
    )